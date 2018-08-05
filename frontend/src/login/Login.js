//Render Prop
import React from 'react';
import { Formik } from 'formik';
import Home  from '../home/Home.js';

const AuthenticationApi = {
    authenticate(email, password, loginSuccess) {
        var params = {
                email: email,
                password: password
        }

        const searchParams = Object.keys(params).map((key) => {
            return encodeURIComponent(key) + '=' + encodeURIComponent(params[key]);
        }).join('&');

        fetch('/api/login', {
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
                'Accept': 'application/json',
            },
            method: 'post',
            body: searchParams
        })
        .then(response => Promise.all([response, response.json()]))
        .then(([response, json]) => {
            if(response.ok) {
                loginSuccess(json);
            } else {
                loginSuccess(json);
                //throw new Error(json);
            }
        })
        .catch(console.error);
    }
};

const LoginForm = ({loginSuccess}) => (
    <div>
    <h1>My Form</h1>
    <p>This can be anywhere in your application</p>
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
                AuthenticationApi.authenticate(values.email, values.password, loginSuccess)
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
    state = {};

    loginSuccess = (json) => {
        this.setState({ authenticated: true });
    }

    render() {
        if(this.state.authenticated) {
            return (
                <div>
                    <p>Not home</p>
                </div>
            );
        }
        return (
            <div>
                <LoginForm loginSuccess={ this.loginSuccess } />
            </div>
        );
    }
}

export default Login;
