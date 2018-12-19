import React, { Component } from 'react';
import PropTypes from 'prop-types';

// material ui components
import { SortingState, IntegratedSorting, FilteringState, IntegratedFiltering } from '@devexpress/dx-react-grid';
import { Grid, Table, TableHeaderRow, TableFilterRow } from '@devexpress/dx-react-grid-material-ui';

import { withStyles } from '@material-ui/core/styles';

// components
import HeadlineWithAction from '../../common/HeadlineWithAction'
import { displayActionMessage } from '../../common/ActionNotifier.js'
import ReauthenticateModal from '../../login/ReauthenticateModal'
import AuthenticationService from '../../authentication/AuthenticationService'
import { displayErrorMessage } from '../../error/ErrorNotifier';

import EventTeamAdd from './EventTeamAdd'

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

    state = {
        addTeamDialogOpen: false,
    };

    componentDidMount() {
        this.fetchTeams();
    }

    fetchTeams = () => {   	
        this.AuthService.fetch('/api/events/' + this.eventId + '/teams', {})
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
    			"name" : row.nameWithCaptain, 
                "parent": row.parentEventTeam ? row.parentEventTeam.name : ''
				};
    		});
    }

    openAddTeamDialog() {
        this.setState({addTeamDialogOpen: true});
    }

    onCloseAddTeamDialog() {
        this.setState({addTeamDialogOpen: false});   
    }

    onTeamAdded(eventTeam) {
        this.setState({addTeamDialogOpen: false});

        // display a notification indicating we have added a new team
        let message = 'Added ' + eventTeam.name;
        let actionUri = '/event-teams/' + eventTeam.eventTeamId;
        displayActionMessage({message, actionUri});

        // refetch the event with updated team information
        this.fetchTeams();
    }


	render() {
        const { reauthenticate, addTeamDialogOpen } = this.state;
		return (
            <React.Fragment>
                {reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />}
                {addTeamDialogOpen && <EventTeamAdd 
                    eventId={this.eventId} 
                    onClosed={this.onCloseAddTeamDialog.bind(this)} 
                    onAdded={this.onTeamAdded.bind(this)} />
                }
                <HeadlineWithAction 
                    headline="Teams" 
                    headlineVariant="h6" 
                    buttonLabel="Add Team"
                    buttonOnClick={this.openAddTeamDialog.bind(this)}
                    buttonSize="small"
                    />
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
	          		<FilteringState defaultFilters={[]} />
	          		<IntegratedFiltering />
				    <Table />
				    <TableHeaderRow showSortingControls />
				    <TableFilterRow />
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