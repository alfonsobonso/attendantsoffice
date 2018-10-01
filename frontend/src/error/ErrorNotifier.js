import React from 'react';
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import ErrorIcon from '@material-ui/icons/Error';
import { withStyles } from '@material-ui/core/styles';


let displayErrorMessageFn;

const contentStyles = theme => ({
  backgroundColor: theme.palette.error.dark,
  icon: {
    fontSize: 20,
  },
  iconVariant: {
    opacity: 0.9,
    marginRight: theme.spacing.unit,
  },
  message: {
    display: 'flex',
    alignItems: 'center',
  },
});

function ErrorNotifierContent(props) {
  	const { classes, message } = props;
  	
  	return (
    	<SnackbarContent className={classes}
            variant="error"
      		aria-describedby="client-snackbar"
      		message={
        		<span id="client-snackbar" className={classes.message}>
          		<ErrorIcon className={classes.icon} />
          		{message}
        		</span>
			}
	    />
  	);
}

const ErrorNotifierWrapper = withStyles(contentStyles)(ErrorNotifierContent);

const notifierStyles = theme => ({
  margin: {
    margin: theme.spacing.unit,
  },
});


class ErrorNotifier extends React.Component {
	state = {
    	open: false,
    	message: '',
  	};

  	componentDidMount() {
    	displayErrorMessageFn = this.displayErrorMessage;
  	}

  	displayErrorMessage = ({ message }) => {
    	this.setState({
      		open: true,
      		message,
    	});
  	};

  	handleSnackbarClose = () => {
    	this.setState({
      		open: false,
      		message: '',
    	});
  	};

	render() {
    	const message = (
      		<span
        		id="snackbar-message-id"
        		dangerouslySetInnerHTML={{ __html: this.state.message }}
      		/>
    	);

    	return (
      		<Snackbar
        		anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        		autoHideDuration={3000}
        		onClose={this.handleSnackbarClose}
        		open={this.state.open}
        		SnackbarContentProps={{
          			'aria-describedby': 'snackbar-message-id',
        		}}
        	>
        		<ErrorNotifierWrapper message={message}	/>
	      	</Snackbar>
      	);
  	}
}

export function displayErrorMessage({ message }) {
	displayErrorMessageFn({ message });
}

export default withStyles(notifierStyles)(ErrorNotifier);
