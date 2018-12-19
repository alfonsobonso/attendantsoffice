import React from 'react';
import PropTypes from 'prop-types';

import { withStyles } from '@material-ui/core/styles';

// components
import AuthenticationService from '../../authentication/AuthenticationService'
import { displayErrorMessage } from '../../error/ErrorNotifier';
import MaterialReactSelect from '../../common/MaterialReactSelect'

// create a select with the fetched congregation list

const styles = theme => ({
});

class EventTeamSelect extends React.Component {
	constructor(props) {
        super();        
        this.AuthService = new AuthenticationService();
        this.eventId = props.eventId;
    }

    state = { };

    componentDidMount() {
        this.fetchEventTeamList();        
    }

    fetchEventTeamList = () => {
        this.AuthService.fetch('/api/events/' + this.eventId + '/teams', {})
        .then(response => {
            if(response.ok) {
                response.json().then((json) => {
                    this.populateEventTeamList(json);
                })
            } else if (response.status === 401) {
                this.setState({reauthenticate: true})
            } else {
                // something has gone wrong
                // We expect the owning component to include the ErrorNotifier to dusplay the message
                displayErrorMessage({ message: 'Failed to fetch the event team list' });
            }
        });
    }

    populateEventTeamList = (json) => {
        var mapped = json.map((item, index) => {
            return { value: item.eventTeamId, label: item.name }
        })

        this.setState({'eventTeamList': mapped})
    }


	render() { 
		const { classes, theme, id, name, value, label, placeholder, onChange, onBlur } = this.props;
        const { eventTeamList } = this.state;

        return(
			<MaterialReactSelect 
            	classes={classes}
				theme={theme}
                id={id} 
                name={name}
                value={value}
                options={eventTeamList}
                label= {label || "Team"}
                placeholder= {placeholder || "Select Team"}
                onChange={onChange}
                onBlur={onBlur} />
        );
 	}
}


EventTeamSelect.propTypes = {
	classes: PropTypes.object.isRequired,
	theme: PropTypes.object.isRequired,
	eventId: PropTypes.object.isRequired,
	value: PropTypes.object,
	id: PropTypes.string,
	name: PropTypes.string,
	options: PropTypes.array,
	label: PropTypes.string,
	placeholder: PropTypes.string,
	onChange: PropTypes.func.isRequired,
	onBlur: PropTypes.func.isRequired,
};

export default withStyles(styles, { withTheme: true })(EventTeamSelect);
