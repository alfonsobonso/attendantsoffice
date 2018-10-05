import React from 'react';
import PropTypes from 'prop-types';

import classNames from 'classnames';

import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import IconButton from '@material-ui/core/IconButton';

import CloseIcon from '@material-ui/icons/Close';
import ErrorIcon from '@material-ui/icons/Error';


import { withStyles } from '@material-ui/core/styles';


let displayErrorMessageFn;

const contentStyles = theme => ({
    error: {
        backgroundColor: theme.palette.error.dark,
    },
    icon: {
        fontSize: 20,
        opacity: 0.9,
        marginRight: theme.spacing.unit,
    },
    message: {
        display: 'flex',
        alignItems: 'center',
    },
});

function ErrorNotifierContent(props) {
  	const { classes, className, message, onClose, ...other } = props;
  	
  	return (
    	<SnackbarContent className={classNames(classes.error, className)}
            aria-describedby="client-snackbar"
      		message={
                <span id="client-snackbar" className={classes.message}>
                    <ErrorIcon className={classes.icon} />
                    {message}
                </span>
            }
            action={[
                <IconButton
                    key="close"
                    aria-label="Close"
                    color="inherit"
                    className={classes.close}
                    onClick={onClose}
                >
                <CloseIcon className={classes.icon} />
                </IconButton>,
            ]}
      {...other}
	    />
  	);
}

ErrorNotifierContent.propTypes = {
    classes: PropTypes.object.isRequired,
    className: PropTypes.string,
    message: PropTypes.node,
    onClose: PropTypes.func,
};

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
        const { classes } = this.props;
        const { message, open } = this.state;

    	return (
      		<Snackbar
        		anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        		autoHideDuration={5000}
        		open={open}
        	>
        		<ErrorNotifierWrapper 
                    message={message} 
                    className={classes.margin}
                    onClose={this.handleSnackbarClose}
                />
	      	</Snackbar>
      	);
  	}
}

export function displayErrorMessage({ message }) {
	displayErrorMessageFn({ message });
}

export default withStyles(notifierStyles)(ErrorNotifier);
