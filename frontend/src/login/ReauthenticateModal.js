import React from 'react';
import PropTypes from 'prop-types';

import { Formik } from 'formik';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import IconButton from '@material-ui/core/IconButton';
import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import InputLabel from '@material-ui/core/InputLabel';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';

import * as Yup from 'yup';

// components
import AuthenticationService from '../authentication/AuthenticationService'
import { displayErrorMessage } from '../error/ErrorNotifier';

// when the authentication is lost while the user is perform an action we block the activity and request they re-authenticate
// if the local storage still has their email address we use that - this is preferred since it forces
// them to log in as the same user, and not worry about them being on a page the new user doesn't have permissions
// to view
class ReauthenticateModal extends React.Component {
	constructor(props) {
        super();
        this.AuthService = new AuthenticationService();
        this.onReauthenticated = props.onReauthenticated;
        this.AuthService.prepareReauthenticate();
    }

	state = {
    	open: true,
  	};

  	// after login close this model and notify the parent they can re-initialise something
  	loginSuccess = () => {
    	this.setState({ open: false });
    	this.onReauthenticated();
  	};

  	handleClickShowPassword = () => {
        this.setState(state => ({ showPassword: !state.showPassword }));
    };

  	render() {
    	return (       
        	<Dialog open={this.state.open} aria-labelledby="form-dialog-title">
        		<DialogTitle id="form-dialog-title">Authentication Expired</DialogTitle>
          		<DialogContent>
            		<DialogContentText>
              			Your authenticate credentials have expired. Please log in again.
            		</DialogContentText>
            		<Formik
                        initialValues = {{ password: '', }}
                        validationSchema = {
                             Yup.object().shape({
                                password: Yup.string().required().min(8)
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

                                this.AuthService.reauthenticate(values.password, this.loginSuccess, submitFailure)
                                setSubmitting(false);
                            }
                        }
                        render = {
                            ({values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting, }) => (
                                <form onSubmit={handleSubmit}>
                                    <FormControl margin="normal" required fullWidth>
                                        <InputLabel htmlFor="password">Password</InputLabel>
                                        <Input name="password" id="password" 
                                            type={this.state.showPassword ? 'text' : 'password'}
                                            onChange={handleChange} onBlur={handleBlur} 
                                            value={values.password} autoComplete="current-password" 
                                            endAdornment={
                                              <InputAdornment position="end">
                                                <IconButton
                                                  aria-label="Toggle password visibility"
                                                  onClick={this.handleClickShowPassword}
                                                  onMouseDown={this.handleMouseDownPassword}
                                                >
                                                  {this.state.showPassword ? <VisibilityOff /> : <Visibility />}
                                                </IconButton>
                                              </InputAdornment>
                                            }
                                            />
                                            {touched.password && errors.password && <FormHelperText id="password-text" error>{errors.password}</FormHelperText>}
                                    </FormControl>

                                    <Button type="submit" fullWidth variant="contained" color="primary" 
                                        disabled={isSubmitting}>
                                        Sign in
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

ReauthenticateModal.propTypes = {
	onReauthenticated: PropTypes.func.isRequired,
};


export default ReauthenticateModal;
