import React, { Component } from 'react';
import PropTypes from 'prop-types';

// material ui components
import Paper from '@material-ui/core/Paper';

import { SortingState, IntegratedSorting } from '@devexpress/dx-react-grid';
import { Grid, Table, TableHeaderRow } from '@devexpress/dx-react-grid-material-ui';

import { withStyles } from '@material-ui/core/styles';

// components
import ReauthenticateModal from '../../login/ReauthenticateModal.js'
import AuthenticationService from '../../authentication/AuthenticationService.js'
import Headline from '../../common/Headline.js'

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

// the number of event teams is small, so the data is loaded in a single request, 
// and the sorting/filtering is controlled client side. 
// We are using the uncontrolled-mode, where the grid itself manages state.
class EventTeams extends Component {
	constructor(props) {
        super();
        this.AuthService = new AuthenticationService();
        this.classes = props.classes
        this.state = {"rows": []};
    }

    state = {};

    componentDidMount() {
        const { match: { params } } = this.props;
        this.fetchTeams(params.eventId);
    }

    fetchTeams = (eventId) => {   	
        this.AuthService.fetch('/api/events/' + eventId + '/teams', {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                	var transformed = this.transformEventTeamRows(json);
                	this.setState({"rows": transformed });
            	});
            } else if (response.status === 401) {
            	this.setState({reauthenticate: true})
            } else {
                alert("Request failed with error code: " + response.status);
            }
        });
    };

	transformEventTeamRows(rows) {
    	return rows.map(row => {
    		return 	{  
    			"id": row.eventTeamId, 
    			"name" : row.name, 
                "parent": row.parentEventTeam?row.parentEventTeam.name:''
				};
    		});
    }


	render() {
		return (
            <React.Fragment>
                <Headline headline="Event Teams" />
    			<Paper className={this.classes.root}>
                    {this.state.reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />}
    				<Grid
    				    rows={this.state.rows}
    				    columns={[
    				      	{ name: 'id', title: 'ID' },
    				      	{ name: 'name', title: 'Name' },
                            { name: 'parent', title: 'Parent' },
    				    ]}>
    				    <SortingState
    	            		defaultSorting={[{ columnName: 'id', direction: 'asc' }]}
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

EventTeams.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(EventTeams);