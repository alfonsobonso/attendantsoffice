import React, { Component } from 'react';
import PropTypes from 'prop-types';

import { Link } from 'react-router-dom'

// material ui components
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';

import { SortingState, IntegratedSorting, DataTypeProvider } from '@devexpress/dx-react-grid';
import { Grid, Table, TableHeaderRow } from '@devexpress/dx-react-grid-material-ui';

import { withStyles } from '@material-ui/core/styles';

// components
import ReauthenticateModal from '../login/ReauthenticateModal.js'
import AuthenticationService from '../authentication/AuthenticationService.js'
import Headline from '../common/Headline.js'

const styles = theme => ({
	root: {
    	width: '100%',
       	overflowX: 'auto',
        marginTop: '16px',
  	},
  	table: {
    	minWidth: 700,
  	},
});

const DateFormatter = ({ value }) => value;
const DateTypeProvider = props => (
  <DataTypeProvider
    formatterComponent={DateFormatter}
    {...props}
  />
);

// the number of events is small, so the data is loaded in a single request, 
// and the sorting/filtering is controlled client side. 
// We are using the uncontrolled-mode, where the grid itself manages state.
class Events extends Component {
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
        this.AuthService.fetch('/api/events', {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                	var transformed = this.transformEventRows(json);
                	this.setState({"rows": transformed });
            	});
            } else if (response.status === 401) {
            	this.setState({reauthenticate: true})
            } else {
                alert("Request failed with error code: " + response.status);
            }
        });
    };

	transformEventRows(rows) {
    	return rows.map(row => {
            let url = '/events/' + row.eventId
    		return 	{  
    			"id": <Button color="primary" size="small" component={Link} to={url}>{row.eventId}</Button>, 
    			"name" : row.name, 
    			"location": row.location,
    			"startDate": row.startDate,
    			"endDate": row.endDate,
    			"status": row.eventStatus
				};
    		});
    }


	render() {
		return (
            <React.Fragment>
                <Headline headline="Events" />
    			<Paper className={this.classes.root}>
                    {this.state.reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />}
    				<Grid
    				    rows={this.state.rows}
    				    columns={[
    				      	{ name: 'id', title: 'ID' },
    				      	{ name: 'name', title: 'Name' },
    				      	{ name: 'location', title: 'Location' },
    				      	{ name: 'startDate', title: 'Start Date' },
    				      	{ name: 'endDate', title: 'End Date' },
    				      	{ name: 'status', title: 'Status' },
    				    ]}>
    				    <DateTypeProvider
    	            		for={['startDate', 'endDate']}
    	          		/>
    				    <SortingState
    	            		defaultSorting={[{ columnName: 'startDate', direction: 'desc' }]}
    	          		/>
    	          		<IntegratedSorting />
    				    <Table />
    				    <TableHeaderRow showSortingControls />
    			  	</Grid>
    		  	</Paper>
            </React.Fragment>
		);
	}
}

Events.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(Events);