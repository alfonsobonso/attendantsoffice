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
import NativeSelect from '@material-ui/core/NativeSelect';

import { withStyles } from '@material-ui/core/styles';

import * as Yup from 'yup';

// components
import AuthenticationService from '../authentication/AuthenticationService'
import ReauthenticateModal from '../login/ReauthenticateModal.js'
import { displayErrorMessage } from '../error/ErrorNotifier';

const styles = theme => ({
});

// modal dialog form to enable us to create a new event information
// this is a cut-down version of the EventEdit
class EventEdit extends React.Component {
	constructor(props) {
        super();        
        this.AuthService = new AuthenticationService();
        this.onUpdated = props.onUpdated;
        this.onClosed = props.onClosed;
        this.event = props.event;
    }

	state = { };

	submitEventUpdate = (eventId, values, submitSuccess, submitFailure) => {
        this.AuthService.fetch('/api/events/' + eventId, { 
            method: 'post',
            body: JSON.stringify(values)
        })
        .then(response => {
            if(response.ok) {
            	submitSuccess();
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
    updateSuccess = () => {
        this.onUpdated();
    };

    handleClose = () => {
        this.onClosed();
    };

    render() { 
        if(this.state.reauthenticate) {
            return (
                <ReauthenticateModal onReauthenticated={function() { return } } />               
            );
        }

        let event = this.event;
    	return (       
        	<Dialog
          		open={true}
          		onClose={this.handleClose}
          		aria-labelledby="form-dialog-title"
        	>
        		<DialogTitle id="form-dialog-title">Edit Event</DialogTitle>
          		<DialogContent>
            		<Formik
                        initialValues = {{ 
                            name: event.name,
                            location: event.location,
                            startDate: event.startDate,
                            endDate: event.endDate,
                            eventStatus: event.eventStatus
                        }}
                        validationSchema = {
                             Yup.object().shape({
                                name: Yup.string()
                                	.required('First name is required')
                                	.min(2, 'First name must be at least 2 characters'),
                                location: Yup.string()
                                	.required('Last name is required')
                                	.min(2, 'Last name must be at least 2 characters'),
                                startDate: Yup.date().required(),
                                endDate: Yup.date().required(),
                            })
                        }
                        onSubmit = {
                            (values, { setSubmitting, setErrors }) => {
                                const submitFailure = (json) => {
                                    let errors = {};
                                    if(json.code === 'DuplicateEventName') {
                                        errors.name = json.message;
                                    } else if(json.code === 'InvalidEventDate') {
                                        errors.startDate = json.message;
                                    } else {
                                        displayErrorMessage({ message: 'Unexpected error:' + json.code });
                                    }
                                    setErrors(errors);
                                }

                                this.submitEventUpdate(event.eventId, values, this.updateSuccess, submitFailure);
                                setSubmitting(false);
                            }
                        }
                        render = {
                            ({form, values, errors, touched, handleChange, setFieldValue, setFieldTouched, handleBlur, handleSubmit, isSubmitting, }) => (
                                <form onSubmit={handleSubmit}>
                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="name">Name</InputLabel>
                                            <Input id="name" name="name" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.name} autoFocus />
                                    </FormControl>
                                    {touched.name && errors.name && <FormHelperText id="name-text" error>{errors.name}</FormHelperText>}

                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="location">Location</InputLabel>
                                            <Input id="location" name="location" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.location} />
                                    </FormControl>
                                    {touched.location && errors.location && <FormHelperText id="location-text" error>{errors.location}</FormHelperText>}

                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="startDate">Start Date</InputLabel>
                                            <Input id="startDate" name="startDate" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.startDate} />
                                    </FormControl>
                                    {touched.startDate && errors.startDate && <FormHelperText id="startDate-text" error>{errors.startDate}</FormHelperText>}

                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="endDate">Start Date</InputLabel>
                                            <Input id="endDate" name="endDate" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.endDate} />
                                    </FormControl>
                                    {touched.endDate && errors.endDate && <FormHelperText id="endDate-text" error>{errors.endDate}</FormHelperText>}
                                    
                                    <FormControl margin="normal" fullWidth>
                                        <InputLabel htmlFor="eventStatus">Status</InputLabel>
                                        <NativeSelect name="eventStatus" value={values.eventStatus} onChange={handleChange} onBlur={handleBlur}>
                                            <option value="ANNOUNCED">Announced</option>
                                            <option value="COMPLETED">Completed</option>
                                        </NativeSelect>
                                    </FormControl>

                                    <Button type="submit" fullWidth variant="contained" color="primary" 
                                        disabled={isSubmitting}>
                                        Update
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

EventEdit.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
    onUpdated: PropTypes.func.isRequired,
    onClosed: PropTypes.func.isRequired,
};

export default withStyles(styles, { withTheme: true })(EventEdit);

