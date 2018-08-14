import React, { Component } from 'react';
import AuthenticationService from '../authentication/AuthenticationService.js'
import { Formik } from 'formik';

class TokenAccessService {

	fetchTokenStatus(token, statusResult) {
		fetch('/api/authentication/access-token/' + token + '/status', {
            method: 'get',
            'Accept': 'application/json',
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
            'Accept': 'application/json',
            credentials: 'same-origin'
        })
        .then(response => Promise.all([response, response.json()]))
        .then(([response, json]) => {
            if(response.ok) {
                updateSuccess();
            }
        })
        .catch(console.error);
	}

}

const TokenAccessForm = ({token, AccessService, updateSuccess, updateFailure}) => (
	<div>
    <h1>Create password</h1>
    <Formik
        initialValues = {{ password: '', passwordRepeat: '', }}
        validate = {
            values => {
            	// TODO: minimum password strength check
                let errors = {};
                if (!values.password) {
                    errors.password = 'Required';
                } else if (!values.passwordRepeat) {
					errors.passwordRepeat = 'Required';
                } else if(values.password !== values.passwordRepeat) {
                	errors.password = 'Passwords must match';
                }
                return errors;
            }
        }
        onSubmit = {
            (values, { setSubmitting, setErrors }) => {
                AccessService.updatePassword(token, values.password, updateSuccess);
                setSubmitting(false);
            }
        }
        render = {
            ({values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting, }) => (
                <form onSubmit={handleSubmit}>
                <input type="password" name="password" onChange={handleChange} onBlur={handleBlur} value={values.password} />
                {touched.password && errors.password && <div>{errors.password}</div>}
                <input type="password" name="passwordRepeat" onChange={handleChange} onBlur={handleBlur} value={values.passwordRepeat} />
                {touched.passwordRepeat && errors.passwordRepeat && <div>{errors.passwordRepeat}</div>}
                <button type="submit" disabled={isSubmitting}>
                Submit
                </button>
                </form>
            )
        }
    />
    </div>
);

class TokenAccess extends Component {
	constructor() {
        super();
        this.AuthService = new AuthenticationService();
        this.AccessService = new TokenAccessService();
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

    render() {
		if(this.state.tokenStatus === 'VALID') {
			return (
				<div>
                	<TokenAccessForm 	token = { this.state.token } 
                						AccessService = { this.AccessService }
										updateSuccess = { this.updateSuccess }
										updateFailure = { this.updateFailure }
                						/>
										}
            	</div>
        	)
		} else if(this.state.tokenStatus === 'UNRECOGNISED') {
			return (
				<div>TBD: Status UNRECOGNISED</div>
        	)
        } else if(this.state.tokenStatus === 'EXPIRED') {
			return (
				<div>TBD: Status EXPIRED</div>
        	)
		} else {
			return (
				<div>TBD: Status ALREADY_USED</div>
        	)
		}
    }
}

export default TokenAccess;