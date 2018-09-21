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

import * as Yup from 'yup';

// components
import AuthenticationService from '../authentication/AuthenticationService'
import ReauthenticateModal from '../login/ReauthenticateModal.js'
import { displayErrorMessage } from '../error/ErrorNotifier';

// modal dialog form to enable us to edit the core user information
// it can be used in a couple of modes - when a user edits their own details, and an admin user editing someone else's.
class UserEdit extends React.Component {
	constructor(props) {
        super();        
        this.AuthService = new AuthenticationService();
        this.classes = props.classes;
        this.onUpdated = props.onUpdated;
        this.onClosed = props.onClosed;
        this.user = props.user;
    }

    state = { };

  	// after login close this model and notify the parent they can re-initialise something
  	updateSuccess = () => {
    	this.onUpdated();
  	};

    submitUserUpdate(userId, values, submitSuccess, submitFailure) {
        this.AuthService.fetch('/api/users/' + userId, { 
            method: 'post',
            body: values
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

    handleClose = () => {
        this.onClosed();
    };

  	render() { 
        if(this.state.reauthenticate) {
            return (
                <ReauthenticateModal onReauthenticated={function() { return } } />               
            );
        }

        let user = this.user;
    	return (       
        	<Dialog
          		open={true}
          		onClose={this.handleClose}
          		aria-labelledby="form-dialog-title"
        	>
        		<DialogTitle id="form-dialog-title">Edit User</DialogTitle>
          		<DialogContent>
            		<Formik
                        initialValues = {{ 
                            firstName: user.firstName,
                            lastName: user.lastName,
                            email: user.email,
                            mobilePhone: user.mobilePhone || '',
                            homePhone: user.homePhone || '',
                            congregationId: user.congregation.congregationId,
                            userStatus: user.userStatus,
                            position: user.position,
                            role: user.role
                        }}
                        validationSchema = {
                             Yup.object().shape({
                                firstName: Yup.string().required().min(2),
                                lastName: Yup.string().required().min(2),
                                email: Yup.string().required().email()
                            })
                        }
                        onSubmit = {
                            (values, { setSubmitting, setErrors }) => {
                                const submitFailure = (json) => {
                                    let errors = {};
                                    if(json.code === 'UserNotFound') {
                                        errors.email = 'unrecognised email address';
                                    } else if(json.code === 'WrongPassword') {
                                        errors.password = 'wrong password';
                                    } else {
                                        displayErrorMessage({ message: 'Unexpected error:' + json.code });
                                    }
                                    setErrors(errors);
                                }
                                this.submitUserUpdate(user.userId, values, this.updateSuccess, submitFailure);
                                setSubmitting(false);
                            }
                        }
                        render = {
                            ({values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting, }) => (
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
                                            <InputLabel htmlFor="congregationId">Congregation</InputLabel>
                                            <Input id="congregationId" name="congregationId" autoComplete="home" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.congregationId} />
                                    </FormControl>
                                    {touched.congregationId && errors.congregationId && <FormHelperText id="congregationId-text" error>{errors.congregationId}</FormHelperText>}

                                    <FormControl margin="normal" fullWidth>
                                        <InputLabel htmlFor="userStatus">Status</InputLabel>
                                        <NativeSelect name="userStatus" value={values.userStatus}>
                                            <option value="DISABLED">Disabled</option>
                                        </NativeSelect>
                                    </FormControl>

                                    <FormControl margin="normal" fullWidth>
                                        <InputLabel htmlFor="position">Position</InputLabel>
                                        <NativeSelect name="position" value={values.position}>
                                            <option value="BAPTISEDBRO">Baptised Brother</option>
                                            <option value="BAPTISEDSIS">Baptised Sister</option>
                                            <option value="MS">Ministerial Servant</option>
                                            <option value="ELDER">Elder</option>
                                        </NativeSelect>
                                    </FormControl>

                                    <FormControl margin="normal" fullWidth>
                                        <InputLabel htmlFor="role">Role</InputLabel>
                                        <NativeSelect name="role" value={values.role}>
                                            <option value="USER">User</option>
                                            <option value="ADMIN">Admin</option>
                                        </NativeSelect>
                                    </FormControl>

                                    <Button type="submit" fullWidth variant="raised" color="primary" 
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

UserEdit.propTypes = {
    onUpdated: PropTypes.func.isRequired,
    onClosed: PropTypes.func.isRequired,
};


export default UserEdit;
