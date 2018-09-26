import React, { Component } from 'react';
import PropTypes from 'prop-types';


// material ui components
import Paper from '@material-ui/core/Paper';

import { PagingState, CustomPaging, FilteringState } from '@devexpress/dx-react-grid';
import { SortingState } from '@devexpress/dx-react-grid';
import { Grid, Table, TableHeaderRow, PagingPanel, TableFilterRow } from '@devexpress/dx-react-grid-material-ui';

import { withStyles } from '@material-ui/core/styles';

// components
import ReauthenticateModal from '../login/ReauthenticateModal.js'
import AuthenticationService from '../authentication/AuthenticationService.js'
import HeadlineWithAction from '../common/HeadlineWithAction.js'

const styles = theme => ({
    root: {
    	width: '100%',
    	overflowX: 'auto',
        marginTop: '16px',
  	},
  	table: {
    	minWidth: 700,
  	},
    header: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: '8px',
        marginBottom: '16px',
    },
    headerButton: {
        marginLeft: 'auto',
    }
});


class Users extends Component {
	constructor(props) {
        super();
        this.AuthService = new AuthenticationService();
        this.classes = props.classes
        this.state = {
        	rows: [], 
        	filters: [],
        	sorting: [{ columnName: 'id', direction: 'asc' }],
        	totalCount: 0,
        	totalPages: 0,
      		pageSize: 25,
      		currentPage: 0,
      		pageSizes: [25, 50, 100]
        };
        this.componentDidMount = this.componentDidMount.bind(this);     // called in onReauthenticated 
    }

    state = {};

    componentDidMount() {
    	this.fetchData(this.state.filters, this.state.sorting, this.state.currentPage, this.state.pageSize);
    }   

    openAddDialog = () => {
        this.setState({add: true});
    }

    // listing functionality
    changeSorting = (sorting) => {
    	// re-sorting goes back to the first page
		this.fetchData(this.state.filters, sorting, 0, this.state.pageSize);
	};

	changeCurrentPage = (page) => {
		this.fetchData(this.state.filters, this.state.sorting, page, this.state.pageSize);
	}

	pageSizeChange = (pageSize) => {
		// changing page size goes back to the first page
		this.fetchData(this.state.filters, this.state.sorting, 0, pageSize);
	}

	changeFilters = (filters) => {
		// filtering results goes back to the first page
		this.fetchData(filters, this.state.sorting, 0, this.state.pageSize);
	}

    fetchData = (filters, sorting, currentPage, pageSize) => {
    	// we only support single column sorting
    	let singleSort = sorting[0];
    	let params = {
            "sortDirection": singleSort.direction.toUpperCase(),
		    "sortBy": singleSort.columnName,
            "page": currentPage,
            "pageSize": pageSize
		}
		// currently we only support a contains style filter, so no need to look at the operation value.
		for (var i = 0; i < filters.length; i++) {
			let columnName = filters[i].columnName;
			let value = filters[i].value;
			params[columnName] = value;
		}

    	let esc = encodeURIComponent
		let query = Object.keys(params)
            .map(k => esc(k) + '=' + esc(params[k]))
            .join('&')
    	
        this.AuthService.fetch('/api/users?' + query, {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                	var transformed = this.transformUserRows(json.items);
                	this.setState({
                		rows: transformed,
                		totalCount: json.totalCount,
                		totalPages: json.totalPages,
                		sorting: sorting, 
                		pageSize: pageSize,
                		currentPage: currentPage
                	});
            	});
            } else if (response.status === 401) {
                this.setState({reauthenticate: true})
            } else {
                alert("Request failed with error code: " + response.status);
            }
        });
    };

    transformUserRows(rows) {
    	return rows.map(row => {
    		return { 
                "id": row.userId, 
                "firstName" : row.firstName, 
                "lastName": row.lastName,
                "homePhone": row.homePhone,
                "mobilePhone": row.mobilePhone,
                "congregation": row.congregation.name
    			};
    		});
    }

	columns = [
	  	{ name: 'id', title: 'ID' },
	  	{ name: 'firstName', title: 'First Name' },
	  	{ name: 'lastName', title: 'Last Name' },
	  	{ name: 'homePhone', title: 'Home' },
	  	{ name: 'mobilePhone', title: 'Mobile' },
	  	{ name: 'congregation', title: 'Congregation' },
	];

	render() {
		const { sorting, rows, totalCount, totalPages, pageSize, currentPage, pageSizes } = this.state;

		return (
            <React.Fragment>
                <HeadlineWithAction headline="Users" buttonLabel="Add new user" buttonOnClick={this.openAddDialog.bind(this)} />
                {this.state.reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount} />}
        		<Paper className={this.classes.root}>
          			<Grid className={this.classes.table}
          				rows={rows}
          				columns={this.columns}
          				>
          				<FilteringState onFiltersChange={this.changeFilters} />
          				<PagingState
    			            currentPage={currentPage}
    			            onCurrentPageChange={this.changeCurrentPage}
    			            pageSize={pageSize}
    			            onPageSizeChange={this.pageSizeChange}
                        />
    			        <CustomPaging totalCount={totalCount} />
          				<SortingState sorting={sorting} onSortingChange={this.changeSorting} />
          				<Table />
    					<TableHeaderRow showSortingControls />
    					<TableFilterRow />
    					<PagingPanel pageSizes={pageSizes} totalPages={totalPages} currentPage={currentPage} />
          			</Grid>
        		</Paper>
            </React.Fragment>
  		);
	}
}

Users.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Users);
