//Render Prop
import React from 'react';
import { Formik } from 'formik';
import AuthenticationService from '../authentication/AuthenticationService.js'

const LoginForm = ({authService, loginSuccess}) => (
    <div>
    <h1>Login</h1>
    <p>Yadda yadda</p>
    <Formik
        initialValues = {{ email: '', password: '', }}
        validate = {
            values => {
                // same as above, but feel free to move this into a class method
                // now.
                let errors = {};
                if (!values.email) {
                    errors.email = 'Required';
                } else if (
                        !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)
                ) {
                    errors.email = 'Invalid email address';
                }
                return errors;
            }
        }
        onSubmit = {
            (values, { setSubmitting, setErrors /* setValues and other goodies */ }) => {
                authService.login(values.email, values.password, loginSuccess)
                setSubmitting(false);
            }
        }
        render = {
            ({values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting, }) => (
                <form onSubmit={handleSubmit}>
                <input type="email" name="email" onChange={handleChange} onBlur={handleBlur} value={values.email} />
                {touched.email && errors.email && <div>{errors.email}</div>}
                <input type="password" name="password" onChange={handleChange} onBlur={handleBlur} value={values.password} />
                {touched.password && errors.password && <div>{errors.password}</div>}
                <button type="submit" disabled={isSubmitting}>
                Submit
                </button>
                </form>
            )
        }
    />
    </div>
);


class Login extends React.Component {
    constructor() {
        super();
        this.AuthService = new AuthenticationService();
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

    render() {
        return (
            <div>
                <LoginForm authService={ this.AuthService } loginSuccess={ this.loginSuccess } />
            </div>
        );
    }
}

export default Login;
