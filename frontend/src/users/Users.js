import React, { Component } from 'react';
import PropTypes from 'prop-types';

import Paper from '@material-ui/core/Paper';

import { SortingState } from '@devexpress/dx-react-grid';
import { Grid, Table, TableHeaderRow } from '@devexpress/dx-react-grid-material-ui';

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
        	sorting: [{ columnName: 'id', direction: 'asc' }]
        };
    }

    state = {};

    componentDidMount() {
    	this.fetchData(this.state.sorting);
    }   

    changeSorting = (sorting) => {
		this.fetchData(sorting);
	};

    fetchData = (sorting) => {
    	// we only support single column sorting
    	let singleSort = sorting[0];
    	let params = {
		  "sortDirection": singleSort.direction.toUpperCase(),
		  "sortBy": singleSort.columnName
		}
    	let esc = encodeURIComponent
		let query = Object.keys(params)
             .map(k => esc(k) + '=' + esc(params[k]))
             .join('&')
    	
        this.AuthService.fetch('/api/users?' + query, {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                	var transformed = this.transformUserRows(json);
                	this.setState({rows: transformed, sorting: sorting});
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
		const { sorting, rows } = this.state;

		return (
    		<Paper className={this.classes.root}>
      			<Grid className={this.classes.table}
      				rows={rows}
      				columns={this.columns}
      				>
      				<SortingState sorting={sorting} onSortingChange={this.changeSorting} />     				
      				<Table />
					<TableHeaderRow showSortingControls />     				
      			</Grid>
    		</Paper>
  		);
	}
}

Users.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Users);
