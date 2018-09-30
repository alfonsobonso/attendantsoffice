import React from 'react';
import PropTypes from 'prop-types';

import { withStyles } from '@material-ui/core/styles';

// components
import AuthenticationService from '../authentication/AuthenticationService'
import { displayErrorMessage } from '../error/ErrorNotifier';
import MaterialReactSelect from '../common/MaterialReactSelect'

// create a select with the fetched congregation list

const styles = theme => ({
});

class CongregationSelect extends React.Component {
	constructor(props) {
        super();        
        this.AuthService = new AuthenticationService();
    }

    state = { };

    componentDidMount() {
        this.fetchCongregationList();        
    }

    fetchCongregationList = () => {
        this.AuthService.fetch('/api/congregations/list', {})
        .then(response => {
            if(response.ok) {
                response.json().then((json) => {
                    this.populateCongregationList(json);
                })
            } else if (response.status === 401) {
                this.setState({reauthenticate: true})
            } else {
                // something has gone wrong
                // We expect the owning component to include the ErrorNotifier to dusplay the message
                displayErrorMessage({ message: 'Failed to fetch the congregation list' });
            }
        });
    }

    populateCongregationList = (json) => {
        var mapped = json.map((item, index) => {
            return { value: item.id, label: item.name }
        })

        this.setState({'congregationList': mapped})
    }


	render() { 
		const { classes, theme, id, name, value, label, placeholder, onChange, onBlur } = this.props;
        const { congregationList } = this.state;

        return(
			<MaterialReactSelect 
            	classes={classes}
				theme={theme}
                id={id} 
                name={name}
                value={value}
                options={congregationList}
                label= {label || "Congregation"}
                placeholder= {placeholder || "Select Congregation"}
                onChange={onChange}
                onBlur={onBlur} />
        );
 	}
}


CongregationSelect.propTypes = {
	classes: PropTypes.object.isRequired,
	theme: PropTypes.object.isRequired,
	value: PropTypes.object,
	id: PropTypes.string,
	name: PropTypes.string,
	options: PropTypes.array,
	label: PropTypes.string,
	placeholder: PropTypes.string,
	onChange: PropTypes.func.isRequired,
	onBlur: PropTypes.func.isRequired,
};

export default withStyles(styles, { withTheme: true })(CongregationSelect);