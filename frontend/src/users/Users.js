import React, { Component } from 'react';
import PropTypes from 'prop-types';

import Paper from '@material-ui/core/Paper';

import { PagingState, CustomPaging } from '@devexpress/dx-react-grid';
import { SortingState } from '@devexpress/dx-react-grid';
import { Grid, Table, TableHeaderRow, PagingPanel } from '@devexpress/dx-react-grid-material-ui';

import { withStyles } from '@material-ui/core/styles';

import AuthenticationService from '../authentication/AuthenticationService.js'

const styles = theme => ({
	root: {
    	width: '100%',
    	marginTop: theme.spacing.unit * 3,
    	overflowX: 'auto',
  	},
  	table: {
    	minWidth: 700,
  	},
});


class Users extends Component {
	constructor(props) {
        super();
        this.AuthService = new AuthenticationService();
        this.classes = props.classes
        this.state = {
        	rows: [], 
        	sorting: [{ columnName: 'id', direction: 'asc' }],
        	totalCount: 0,
        	totalPages: 0,
      		pageSize: 25,
      		currentPage: 0,
      		pageSizes: [25, 50, 100]
        };
    }

    state = {};

    componentDidMount() {
    	this.fetchData(this.state.sorting, this.state.currentPage, this.state.pageSize);
    }   

    changeSorting = (sorting) => {
    	// re-sorting goes back to the first page
		this.fetchData(sorting, 0, this.state.pageSize);
	};

	changeCurrentPage = (page) => {
		this.fetchData(this.state.sorting, page, this.state.pageSize);	
	}

	pageSizeChange = (pageSize) => {
		// changin page size goes back to the first page
		this.fetchData(this.state.sorting, 0, pageSize);		
	}

    fetchData = (sorting, currentPage, pageSize) => {
    	// we only support single column sorting
    	let singleSort = sorting[0];
    	let params = {
		  "sortDirection": singleSort.direction.toUpperCase(),
		  "sortBy": singleSort.columnName,
		  "page": currentPage,
		  "pageSize": pageSize
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
            	alert("oli to do");
            }
        });
    };

    transformUserRows(rows) {
    	return rows.map(row => {
    		return { "id": row.userId, 
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
    		<Paper className={this.classes.root}>
      			<Grid className={this.classes.table}
      				rows={rows}
      				columns={this.columns}
      				>
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
					<PagingPanel pageSizes={pageSizes} totalPages={totalPages} currentPage={currentPage} />   				
      			</Grid>
    		</Paper>
  		);
	}
}

Users.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Users);
