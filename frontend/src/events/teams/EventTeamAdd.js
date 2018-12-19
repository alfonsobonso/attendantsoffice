import React from 'react';
import PropTypes from 'prop-types';

import { Formik } from 'formik';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';

import { withStyles } from '@material-ui/core/styles';

import * as Yup from 'yup';

// components
import AuthenticationService from '../../authentication/AuthenticationService'
import ReauthenticateModal from '../../login/ReauthenticateModal.js'
import { displayErrorMessage } from '../../error/ErrorNotifier';
import EventTeamSelect from './EventTeamSelect'

const styles = theme => ({
});

// modal dialog form to enable us to create a new team linked to an event
class EventTeamAdd extends React.Component {
	constructor(props) {
        super();        
        this.AuthService = new AuthenticationService();
        this.eventId = props.eventId;
        this.onAdded = props.onAdded;
        this.onClosed = props.onClosed;
    }

	state = { };

	submitEventTeamAdd = (eventId, values, submitSuccess, submitFailure) => {
        this.AuthService.fetch('/api/events/' + eventId + '/teams', { 
            method: 'post',
            body: JSON.stringify(values)
        })
        .then(response => {
            if(response.ok) {
            	response.json().then((json) => {
        			submitSuccess(json);
            	})
            } else if (response.status === 401) {
                this.setState({reauthenticate: true})
            } else if (response.status < 500) {
                response.json().then((json) => {
                    submitFailure(json);
                })
            } else {
                // something has gone wrong
                submitFailure({ "code": "Failed to submit request"});
            }
        });
    }

    // after login close this modal and notify the parent they can re-initialise something
    updateSuccess = (eventTeam) => {
        this.onAdded(eventTeam);
    };

    handleClose = () => {
        this.onClosed();
    };

    render() { 
        const { classes, theme, eventId } = this.props;

        if(this.state.reauthenticate) {
            return (
                <ReauthenticateModal onReauthenticated={function() { return } } />               
            );
        }

    	return (       
        	<Dialog
          		open={true}
          		onClose={this.handleClose}
          		aria-labelledby="form-dialog-title"
        	>
        		<DialogTitle id="form-dialog-title">Add Event Team</DialogTitle>
          		<DialogContent>
            		<Formik
                        initialValues = {{ 
                            name: ''
                        }}
                        validationSchema = {
                             Yup.object().shape({
                                name: Yup.string()
                                	.required('Name is required')
                                	.min(2, 'Name must be at least 2 characters'),
                            })
                        }
                        onSubmit = {
                            (values, { setSubmitting, setErrors }) => {
                                const submitFailure = (json) => {
                                    let errors = {};
                                    if(json.code === 'DuplicateEventTeamName') {
                                        errors.name = json.message;
                                    } else {
                                        displayErrorMessage({ message: 'Unexpected error:' + json.code });
                                    }
                                    setErrors(errors);
                                }

                                // convert the parent team from the select label/value to just the value
                                const payload = {
                                    ...values,
                                    parentEventTeamId: values.parentEventTeamId ? values.parentEventTeamId.value : null,
                                };
                                this.submitEventTeamAdd(eventId, payload, this.updateSuccess, submitFailure);
                                setSubmitting(false);
                            }
                        }
                        render = {
                            ({form, values, errors, touched, handleChange, setFieldValue, setFieldTouched, handleBlur, handleSubmit, isSubmitting, }) => (
                                <form onSubmit={handleSubmit}>
                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="name">Name</InputLabel>
                                            <Input id="firstName" name="name" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.name} autoFocus />
                                    </FormControl>
                                    {touched.name && errors.name && <FormHelperText id="name-text" error>{errors.name}</FormHelperText>}

                                    <FormControl margin="normal" fullWidth>
                                        <EventTeamSelect 
                                            eventId={eventId}
                                            classes={classes}
                                            theme={theme}
                                            id="parentEventTeamId" 
                                            name="parentEventTeamId"
                                            label="Parent team"
                                            value={values.parentEventTeamId}
                                            onChange={setFieldValue}
                                            onBlur={setFieldTouched} />
                                    </FormControl>
                                    {touched.parentEventTeamId && errors.parentEventTeamId && <FormHelperText id="parentEventTeamId-text" error>{errors.parentEventTeamId}</FormHelperText>}

                                    <Button type="submit" fullWidth variant="contained" color="primary" 
                                        disabled={isSubmitting}>
                                        Create
                                    </Button>
                                </form>
                            )
                        }
                    />           		
          		</DialogContent>
        	</Dialog>
    	);
 	}
}

EventTeamAdd.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
    eventId: PropTypes.object.isRequired,
    onAdded: PropTypes.func.isRequired,
    onClosed: PropTypes.func.isRequired,
};

export default withStyles(styles, { withTheme: true })(EventTeamAdd);

