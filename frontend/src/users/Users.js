import React, { Component } from 'react';
import PropTypes from 'prop-types';

import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';

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
        this.state = {"rows": []};
    }

    state = {};

    componentDidMount() {
    	this.fetchData();
    }

    fetchData = () => {
        this.AuthService.fetch('/api/users', {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                	var transformed = this.transformUserRows(json);
                	this.setState({"rows": transformed});
            	});
            } else if (response.status === 401) {
            	this.setState({redirecting: true})
            }
        })
        .then(message => {
            this.setState({message: message});
        });
    };

    transformUserRows(rows) {
    	return rows.map(row => {
    		return { "id": row.userId, "firstName" : row.firstName, "lastName": row.lastName };
    	});
    }

    render() {
    	return (
    		<Paper className={this.classes.root}>
      			<Table className={this.classes.table}>
	        		<TableHead>
	          			<TableRow>
	            			<TableCell>ID</TableCell>
				            <TableCell numeric>First name</TableCell>
				            <TableCell numeric>Last name</TableCell>
	          			</TableRow>
	    			</TableHead>
	        		<TableBody>
		          		{this.state.rows.map(row => {
		            		return (
		              			<TableRow key={row.id}>
		                			<TableCell component="th" scope="row">
		                  				{row.id}
		                			</TableCell>
		                			<TableCell numeric>{row.firstName}</TableCell>
					                <TableCell numeric>{row.lastName}</TableCell>
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
