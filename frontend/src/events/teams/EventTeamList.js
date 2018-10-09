import React, { Component } from 'react';
import PropTypes from 'prop-types';

// material ui components
import { SortingState, IntegratedSorting } from '@devexpress/dx-react-grid';
import { Grid, Table, TableHeaderRow } from '@devexpress/dx-react-grid-material-ui';

import { withStyles } from '@material-ui/core/styles';

// components
import ReauthenticateModal from '../../login/ReauthenticateModal'
import AuthenticationService from '../../authentication/AuthenticationService'
import { displayErrorMessage } from '../../error/ErrorNotifier';

// the number of event teams is small, so the data is loaded in a single request, 
// and the sorting/filtering is controlled client side. 
// We are using the uncontrolled-mode, where the grid itself manages state.
// This component is not a complete page, but is embedded into other pages, such as the event


const styles = theme => ({
  	table: {
    	minWidth: 700,
  	},
});

class EventTeamList extends Component {
	constructor(props) {
        super();
        this.AuthService = new AuthenticationService();
        this.eventId = props.eventId;
        this.state = {"rows": []};
    }

    state = {};

    componentDidMount() {
        this.fetchTeams(this.eventId);
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
            	displayErrorMessage({ message: 'Request failed with error code: ' + response.status });
            }
        });
    };

	transformEventTeamRows(rows) {
    	return rows.map(row => {
    		return 	{  
    			"id": row.eventTeamId, 
    			"name" : row.name, 
                "parent": row.parent?row.parent.name:''
				};
    		});
    }


	render() {
		return (
            <React.Fragment>
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
            </React.Fragment>
		);
	}
}

EventTeamList.propTypes = {
  classes: PropTypes.object.isRequired,
  eventId: PropTypes.number.isRequired,
};

export default withStyles(styles)(EventTeamList);