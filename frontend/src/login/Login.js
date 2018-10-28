import React, { Component } from 'react';
import { Formik } from 'formik';
import PropTypes from 'prop-types';

import { Link } from 'react-router-dom'

// material ui components
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import IconButton from '@material-ui/core/IconButton';
import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import InputLabel from '@material-ui/core/InputLabel';
import LockIcon from '@material-ui/icons/LockOutlined';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import withStyles from '@material-ui/core/styles/withStyles';

import * as Yup from 'yup';

// components
import AuthenticationService from '../authentication/AuthenticationService'
import ErrorBoundary from '../error/ErrorBoundary'
import ErrorNotifier, { displayErrorMessage } from '../error/ErrorNotifier';

const styles = theme => ({
    layout: {
        width: 'auto',
        marginLeft: theme.spacing.unit * 3,
        marginRight: theme.spacing.unit * 3,
        [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
            width: 400,
            marginLeft: 'auto',
            marginRight: 'auto',
        },
    },
    paper: {
        marginTop: theme.spacing.unit * 8,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: `${theme.spacing.unit * 2}px ${theme.spacing.unit * 3}px ${theme.spacing.unit * 3}px`,
    },
    avatar: {
        margin: theme.spacing.unit,
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        marginTop: theme.spacing.unit,
    },
    submit: {
        marginTop: theme.spacing.unit * 3,
    },
    forgottenPassword: {
        marginLeft: 'auto',
        marginTop: theme.spacing.unit
    } 
});

// When the user already has a password set they go to the basic email + password form to authenticate
class Login extends Component {
    constructor(props) {
        super();
        this.AuthService = new AuthenticationService();
        this.classes = props.classes
    }

    
    state = {};

    componentWillMount(){
        if(this.AuthService.isLoggedIn()) {
            this.props.history.replace('/');
        }
    }

    loginSuccess = (json) => {
        this.props.history.replace('/');
    } 

    handleClickShowPassword = () => {
        this.setState(state => ({ showPassword: !state.showPassword }));
    };


    render() {        
        return(
            <ErrorBoundary>
                <ErrorNotifier />
                <main className={this.classes.layout}>
                    <Paper className={this.classes.paper}>
                        <Avatar className={this.classes.avatar}>
                            <LockIcon />
                        </Avatar>
                        <Typography variant="headline">Sign in</Typography>
                        <Formik
                            initialValues = {{ email: '', password: '', }}
                            validationSchema = {
                                 Yup.object().shape({
                                    email: Yup.string().required().email(),
                                    password: Yup.string().required().min(8)
                                })
                            }
                            onSubmit = {
                                (values, { setSubmitting, setErrors }) => {
                                    const submitFailure = (json) => {
                                        let errors = {};
                                        if(json.code === 'UserNotFound') {
                                            errors.email = 'Unrecognised email address';
                                        } else if(json.code === 'WrongPassword') {
                                            errors.password = 'Wrong password';
                                        } else if (json.code === 'PasswordNotSetAuthentication') {
                                            errors.password = 'No password set. An email with access token has been sent';
                                        } else {
                                            displayErrorMessage({ message: 'Unexpected error:' + json.code });
                                        }
                                        setErrors(errors);
                                    }

                                    this.AuthService.login(values.email, values.password, this.loginSuccess, submitFailure)
                                    setSubmitting(false);
                                }
                            }
                            render = {
                                ({values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting, }) => (
                                    <form className={this.classes.form} onSubmit={handleSubmit}>
                                        <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="email">Email Address</InputLabel>
                                            <Input id="email" name="email" autoComplete="email" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.email} autoFocus />
                                        </FormControl>
                                        {touched.email && errors.email && <FormHelperText id="email-text" error>{errors.email}</FormHelperText>}

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

                                        <Button type="submit" fullWidth variant="contained" color="primary" className={this.classes.submit} 
                                            disabled={isSubmitting}>
                                            Sign in
                                        </Button>
                                    </form>
                                )
                            }
                        />
                        <Typography variant="body1" gutterBottom align="right" color="inherit" className={this.classes.forgottenPassword}>
                            <Button color="primary" size="small" component={Link} to={'/forgotten-password'}>forgotten my password</Button>
                        </Typography>
                    </Paper>
                </main>
            </ErrorBoundary>
        );
    }
}

Login.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Login);
