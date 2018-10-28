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
import CongregationSelect from './CongregationSelect'

const styles = theme => ({
});

// modal dialog form to enable us to create a new user information
// this is a cut-down version of the UserEdit
class UserAdd extends React.Component {
	constructor(props) {
        super();        
        this.AuthService = new AuthenticationService();
        this.onAdded = props.onAdded;
        this.onClosed = props.onClosed;
    }

	state = { };

	submitUserAdd = (values, submitSuccess, submitFailure) => {
        this.AuthService.fetch('/api/users/', { 
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
    updateSuccess = (user) => {
        this.onAdded(user);
    };

    handleClose = () => {
        this.onClosed();
    };

    render() { 
        const { classes, theme } = this.props;

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
        		<DialogTitle id="form-dialog-title">Add User</DialogTitle>
          		<DialogContent>
            		<Formik
                        initialValues = {{ 
                            firstName: '',
                            lastName: '',
                            email: '',
                            mobilePhone: '',
                            homePhone: '',
                            position: 'BAPTISEDBRO',
                        }}
                        validationSchema = {
                             Yup.object().shape({
                                firstName: Yup.string()
                                	.required('First name is required')
                                	.min(2, 'First name must be at least 2 characters'),
                                lastName: Yup.string()
                                	.required('Last name is required')
                                	.min(2, 'Last name must be at least 2 characters'),
                                email: Yup.string().required().email(),
                                congregationId: Yup.object().required('Congregation is required'),
                                position: Yup.string().required(),
                            })
                        }
                        onSubmit = {
                            (values, { setSubmitting, setErrors }) => {
                                const submitFailure = (json) => {
                                    let errors = {};
                                    if(json.code === 'DuplicateUserEmailAddress') {
                                        errors.email = json.message;
                                    } else {
                                        displayErrorMessage({ message: 'Unexpected error:' + json.code });
                                    }
                                    setErrors(errors);
                                }
                                // convert the congregation name from the select label/value to just the value
                                const payload = {
                                    ...values,
                                    congregationId: values.congregationId.value,
                                };

                                this.submitUserAdd(payload, this.updateSuccess, submitFailure);
                                setSubmitting(false);
                            }
                        }
                        render = {
                            ({form, values, errors, touched, handleChange, setFieldValue, setFieldTouched, handleBlur, handleSubmit, isSubmitting, }) => (
                                <form onSubmit={handleSubmit}>
                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="firstName">First name</InputLabel>
                                            <Input id="firstName" name="firstName" autoComplete="given-name" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.firstName} autoFocus />
                                    </FormControl>
                                    {touched.firstName && errors.firstName && <FormHelperText id="firstName-text" error>{errors.firstName}</FormHelperText>}

                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="lastName">Last name</InputLabel>
                                            <Input id="lastName" name="lastName" autoComplete="family-name" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.lastName} />
                                    </FormControl>
                                    {touched.lastName && errors.lastName && <FormHelperText id="lastName-text" error>{errors.lastName}</FormHelperText>}

                                    <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="email">Email Address</InputLabel>
                                            <Input id="email" name="email" autoComplete="email" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.email} />
                                    </FormControl>
                                    {touched.email && errors.email && <FormHelperText id="email-text" error>{errors.email}</FormHelperText>}

                                    <FormControl margin="normal" fullWidth>
                                            <InputLabel htmlFor="mobilePhone">Mobile Phone Number</InputLabel>
                                            <Input id="mobilePhone" name="mobilePhone" autoComplete="mobile" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.mobilePhone} />
                                    </FormControl>
                                    {touched.mobilePhone && errors.mobilePhone && <FormHelperText id="mobilePhone-text" error>{errors.mobilePhone}</FormHelperText>}

                                    <FormControl margin="normal" fullWidth>
                                            <InputLabel htmlFor="homePhone">Home Phone Number</InputLabel>
                                            <Input id="homePhone" name="homePhone" autoComplete="home" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.homePhone} />
                                    </FormControl>
                                    {touched.homePhone && errors.homePhone && <FormHelperText id="homePhone-text" error>{errors.homePhone}</FormHelperText>}

                                    <FormControl margin="normal" fullWidth>
                                        <CongregationSelect 
                                            classes={classes}
                                            theme={theme}
                                            id="congregationId" 
                                            name="congregationId"
                                            value={values.congregationId}
                                            onChange={setFieldValue}
                                            onBlur={setFieldTouched} />
                                    </FormControl>
                                    {touched.congregationId && errors.congregationId && <FormHelperText id="congregation-text" error>{errors.congregationId}</FormHelperText>}

                                    <FormControl margin="normal" fullWidth>
                                        <InputLabel htmlFor="position">Position</InputLabel>
                                        <NativeSelect name="position" value={values.position}>
                                            <option value="BAPTISEDBRO">Baptised Brother</option>
                                            <option value="BAPTISEDSIS">Baptised Sister</option>
                                            <option value="MS">Ministerial Servant</option>
                                            <option value="ELDER">Elder</option>
                                        </NativeSelect>
                                    </FormControl>

                                    <Button type="submit" fullWidth variant="raised" color="primary" 
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

UserAdd.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
    onAdded: PropTypes.func.isRequired,
    onClosed: PropTypes.func.isRequired,
};

export default withStyles(styles, { withTheme: true })(UserAdd);

