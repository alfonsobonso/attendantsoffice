import React, { Component } from 'react';
import PropTypes from 'prop-types';

import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';

import { withStyles } from '@material-ui/core/styles';

import EnhancedTableHead from '../common/EnhancedTableHead.js'

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
        this.state = {"rows": [], "selected": [], "sortDirection": "asc", "sortBy": "id"};
    }

    state = {};

    componentDidMount() {
    	this.fetchData(this.state.sortDirection, this.state.sortBy);
    }

    handleRequestSort = (event, property) => {
	    const sortBy = property;
	    let sortDirection = 'desc';

	    if (this.state.sortBy === property && this.state.sortDirection === 'desc') {
	    	sortDirection = 'asc';
	    }

	    this.fetchData(sortDirection, sortBy);
	};

    fetchData = (sortDirection, sortBy) => {

    	let params = {
		  "sortDirection": sortDirection.toUpperCase(),
		  "sortBy": sortBy
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
                	this.setState({"rows": transformed, "sortDirection": sortDirection, "sortBy": sortBy});
                	this.setState();
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
	  	{ id: 'id', numeric: true, disablePadding: false, label: 'ID' },
	  	{ id: 'firstName', numeric: false, disablePadding: false, label: 'First Name' },
	  	{ id: 'lastName', numeric: false, disablePadding: false, label: 'Last Name' },
	  	{ id: 'homePhone', numeric: false, disablePadding: false, label: 'Home' },
	  	{ id: 'mobilePhone', numeric: false, disablePadding: false, label: 'Mobile' },
	  	{ id: 'congregation', numeric: false, disablePadding: false, label: 'Congregation' },
	];

    render() {
    	//const { data, sortDirection, sortBy, selected, rowsPerPage, page } = this.state;
    	const { sortDirection, sortBy, selected, rows } = this.state;

    	return (
    		<Paper className={this.classes.root}>
      			<Table className={this.classes.table}>
        			<EnhancedTableHead
        				supportSelect={false}
		              	numSelected={selected.length}
		              	sortDirection={sortDirection}
		              	sortBy={sortBy}
		              	onSelectAllClick={this.handleSelectAllClick}
		              	onRequestSort={this.handleRequestSort}
		              	rowCount={rows.length}
		              	columns={this.columns}
		            />	          			
	        		<TableBody>
		          		{this.state.rows.map(row => {
		            		return (
		              			<TableRow key={row.id}>
		                			<TableCell numeric component="th" scope="row">
		                  				{row.id}
		                			</TableCell>
		                			<TableCell>{row.firstName}</TableCell>
					                <TableCell>{row.lastName}</TableCell>
					                <TableCell>{row.homePhone}</TableCell>
					                <TableCell>{row.mobilePhone}</TableCell>
					                <TableCell>{row.congregation}</TableCell>
		              			</TableRow>
		            		);
			          	})}
	        		</TableBody>
      			</Table>
    		</Paper>
  		);
    }
}

Users.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Users);
