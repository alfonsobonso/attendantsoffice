import React, { Component } from 'react';
import { Formik } from 'formik';
import PropTypes from 'prop-types';

// material ui components
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import LockIcon from '@material-ui/icons/LockOutlined';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import withStyles from '@material-ui/core/styles/withStyles';

import * as Yup from 'yup';

// components
import AuthenticationService from '../authentication/AuthenticationService.js'


class TokenAccessService {

    requestAccessToken(email, requestSuccess, requestFailure) {
        fetch('/api/authentication/access-token', {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email
            }),
            credentials: 'same-origin'
        })
        .then((response) => {
            if(response.ok) {
                requestSuccess();
            } else if (response.status < 500) {
                response.json().then(json => {
                    requestFailure(json);   
                })
            } else {
                // something has gone wrong
                requestFailure({ "code": "Failed to submit request"});
            }
        })
        .catch(console.error);
    }

    fetchTokenStatus(token, statusResult) {
        fetch('/api/authentication/access-token/' + token + '/status', {
            method: 'get',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        })
        .then(response => Promise.all([response, response.json()]))
        .then(([response, json]) => {
            if(response.ok) {
                statusResult(json.status);
            }
        })
        .catch(console.error);
    }

    updatePassword(token, password, updateSuccess) {
        fetch('/api/authentication/access-token/' + token + '/update-password', {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                password
            }),
            credentials: 'same-origin'
        })
        .then(response => {
            if(response.ok) {
                updateSuccess();
            } else {
                response.json().then(json => {
                    alert(JSON.stringify(json));
                })
            }
        })
        .catch(console.error);
    }
}

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
});

class TokenAccess extends Component {
    constructor(props) {
        super();
        this.updateFailure = this.updateFailure.bind(this);
        this.updateSuccess = this.updateSuccess.bind(this);
        this.requestAccessSuccess = this.requestAccessSuccess.bind(this);
        this.AuthService = new AuthenticationService();
        this.AccessService = new TokenAccessService();
        this.classes = props.classes
    }

    componentDidMount() {
        var token = this.state.token;
        this.AccessService.fetchTokenStatus(token, (status) => {
            this.setState({tokenStatus: status});   
        });
    }

    componentWillMount() {
        if(this.AuthService.isLoggedIn()) {
            this.props.history.replace('/');
        } else {
            this.setState({token : this.props.match.params.token}); 
        }
    }

    updateSuccess() {
        // TODO: include data so we can indicate the password was updated 
        this.props.history.replace('/login');
    }

    updateFailure() {
        // suspect the failure was caused by the token not being valid. If we reload the page
        // it should display to the user the reason
        this.AccessService.fetchTokenStatus(this.state.token, (status) => {
            this.setState({tokenStatus: status});   
        });
    }

    requestAccessSuccess() {
        this.setState({requestAccessStatus: 'SUCCESS'});    
    }

    // if this page has a valid token in the url, we given them the form to set/reset their password
    renderValidToken() {
        return (
            <React.Fragment>
                <CssBaseline />
                <main className={this.classes.layout}>
                    <Paper className={this.classes.paper}>
                        <Avatar className={this.classes.avatar}>
                            <LockIcon />
                        </Avatar>
                        <Typography variant="headline">Set your password</Typography>
                        <Formik
                            initialValues = {{ password: '', passwordRepeat: '', }}
                            validationSchema = {
                                 Yup.object().shape({
                                    password: Yup.string()
                                        .required()
                                        .min(8),
                                    passwordRepeat: Yup.string()
                                        .required()
                                        .oneOf([Yup.ref('password')], 'Passwords do not match')
                                })
                            }
                            validate = {
                                values => {
                                    let errors = {};
                                    if (values.password && values.passwordRepeat) {
                                        if(values.password !== values.passwordRepeat) {
                                            errors.passwordRepeat = 'Passwords must match';
                                        } else if (values.password.length < 8) {
                                            errors.password = 'Passwords must be at least 8 characters';
                                        }
                                    }
                                    return errors;
                                }
                            }
                            onSubmit = {
                                (values, { setSubmitting, setErrors }) => {
                                    this.AccessService.updatePassword(this.state.token, values.password, this.updateSuccess);
                                    setSubmitting(false);
                                }
                            }
                            render = {
                                ({values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting, }) => (
                                    <form onSubmit={handleSubmit}>
                                        <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="password">Password</InputLabel>
                                            <Input name="password" type="password" id="password" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.password} />
                                                {touched.password && errors.password && <FormHelperText id="password-text" error>{errors.password}</FormHelperText>}
                                        </FormControl>

                                        <FormControl margin="normal" required fullWidth>
                                            <InputLabel htmlFor="passwordRepeat">Repeat Password</InputLabel>
                                            <Input name="passwordRepeat" type="password" id="password-repeat" onChange={handleChange} onBlur={handleBlur} 
                                                value={values.passwordRepeat} />
                                                {touched.passwordRepeat && errors.passwordRepeat && <FormHelperText id="password-repeat-text" error>{errors.passwordRepeat}</FormHelperText>}
                                        </FormControl>

                                        <Button type="submit" fullWidth variant="raised" color="primary" className={this.classes.submit} 
                                            disabled={isSubmitting}>
                                            Set password
                                        </Button>
                                    </form>
                                )
                            }
                        />
                    </Paper>
                </main>
            </React.Fragment>
        );
    }

    // if this page does not have a valid token in the url, we give them the form to request another (entering their email).
    renderInvalidToken(headline) {
        return (
            <React.Fragment>
                <CssBaseline />
                <main className={this.classes.layout}>
                    <Paper className={this.classes.paper}>
                        <Typography variant="headline">{headline}</Typography>
                        <Typography variant="title" align="center" color="textSecondary" paragraph>
                            Request a new access token be sent to your email address
                        </Typography>
                        <Formik
                            initialValues = {{ email: '', }}
                            validate = {
                                values => {
                                    let errors = {};
                                    if (values.email && !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
                                        errors.email = 'Invalid email address';
                                    }
                                    return errors;
                                }
                            }
                            onSubmit = {
                                (values, { setSubmitting, setErrors }) => {
                                    const submitFailure = (json) => {
                                        let errors = {};
                                        if(json.code === 'UserNotFound') {
                                            errors.email = 'Unrecognised email address';
                                        }
                                        setErrors(errors);
                                    }
                                    this.AccessService.requestAccessToken(values.email, this.requestAccessSuccess, submitFailure)
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

                                        <Button type="submit" fullWidth variant="raised" color="primary" className={this.classes.submit} 
                                            disabled={isSubmitting}>
                                            Request access
                                        </Button>
                                    </form>
                                )
                            }
                        />
                    </Paper>
                </main>
            </React.Fragment>
        );
    }

    // if the page previously had an invalid token, but we have sucessfully requested another
    // give them a message to check their email inbox
    renderTokenAccessRequested() {
        return (
            <React.Fragment>
                <CssBaseline />
                <main className={this.classes.layout}>
                    <Paper className={this.classes.paper}>
                        <Typography variant="headline">Access code requested</Typography>
                        <Typography variant="title" align="center" color="textSecondary" paragraph>
                            Please check your email inbox for the email with access link. 
                            Check your spam folder if it is not there.
                        </Typography>
                    </Paper>
                </main>
            </React.Fragment>
        );
    }

    render() {
        if(this.state.requestAccessStatus === 'SUCCESS') {
            return this.renderTokenAccessRequested();
        } else if(this.state.tokenStatus === 'VALID') {
            return this.renderValidToken();
        } else if(this.state.tokenStatus === 'UNRECOGNISED') {
            return this.renderInvalidToken('Unrecognised access code.')
        } else if(this.state.tokenStatus === 'EXPIRED') {
            return this.renderInvalidToken('Access code expired.');
        } else {
            return this.renderInvalidToken('Access code already used.');
        }
    }
}

TokenAccess.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(TokenAccess);
