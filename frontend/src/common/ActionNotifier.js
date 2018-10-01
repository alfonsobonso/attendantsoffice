import React from 'react';

import { Link } from 'react-router-dom'

import Button from '@material-ui/core/Button';
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';

import { withStyles } from '@material-ui/core/styles';

// Display the result of an action, with an optional link to naviagate
// An example usage might be creating a new User, we leave the page on the listing page so they can create another
// but display this, along with a link to that new user, allowing them to navigate there.
let displayActionMessageFn;

function ActionNotifierContent(props) {
  	const { classes, message, actionUri, actionText } = props;
  	
    let action = actionUri && 
        <Button color="secondary" size="small" component={Link} to={actionUri}>
            {actionText || 'Go'}
        </Button>

  	return (
    	<SnackbarContent className={classes}
      		aria-describedby="client-snackbar"
            message={message}
            action={action}      		
	    />
  	);
}

const notifierStyles = theme => ({
  margin: {
    margin: theme.spacing.unit,
  },
});


class ActionNotifier extends React.Component {
	state = {
    	open: false,
    	message: '',
        actionUri: '',
        actionText: '',
  	};

  	componentDidMount() {
    	displayActionMessageFn = this.displayActionMessage;
  	}

  	displayActionMessage = ({ message, actionUri, actionText }) => {
    	this.setState({
      		open: true,
      		message,
            actionUri, 
            actionText,
    	});
  	};

  	handleSnackbarClose = () => {
    	this.setState({
      		open: false,
      		message: '',
            actionUri: '',
            actionText: '',
    	});
  	};

	render() {
    	return (
      		<Snackbar
        		anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        		autoHideDuration={3000}
        		onClose={this.handleSnackbarClose}
        		open={this.state.open}        		
        	>
        		<ActionNotifierContent message={this.state.message} actionUri={this.state.actionUri} actionText={this.state.actionText} />
	      	</Snackbar>
      	);
  	}
}

export function displayActionMessage({ message, actionUri, actionText }) {
	displayActionMessageFn({ message, actionUri, actionText });
}

export default withStyles(notifierStyles)(ActionNotifier);
