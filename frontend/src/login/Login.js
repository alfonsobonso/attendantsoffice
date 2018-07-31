//Render Prop
import React from 'react';
import { Formik } from 'formik';

const AuthenticationApi = {
        isAuthenticated: false,
        authenticate(email, password) {
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
                    return json;
                } else {
                    throw new Error(json);
                }
            })
            .catch(console.error);

            this.isAuthenticated = true;
        },
        signout(cb) {
            this.isAuthenticated = false;
        }
};


const LoginForm = () => (
        <div>
        <h1>My Form</h1>
        <p>This can be anywhere in your application</p>
        <Formik
        initialValues={{
            email: '',
            password: '',
        }}
        validate={values => {
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
        }}
        onSubmit={(
                values,
                { setSubmitting, setErrors /* setValues and other goodies */ }
        ) => {
            AuthenticationApi.authenticate(values.email, values.password)
            setSubmitting(false);
// LoginToMyApp(values).then(
// user => {
// setSubmitting(false);
// // do whatevs...
// // props.updateUser(user)
// },
// errors => {
// setSubmitting(false);
// // Maybe transform your API's errors into the same shape as Formik's
// setErrors(transformMyApiErrors(errors));
// }
// );

        }}
        render={({
            values,
            errors,
            touched,
            handleChange,
            handleBlur,
            handleSubmit,
            isSubmitting,
        }) => (
                <form onSubmit={handleSubmit}>
                <input
                type="email"
                    name="email"
                        onChange={handleChange}
                onBlur={handleBlur}
                value={values.email}
                />
                {touched.email && errors.email && <div>{errors.email}</div>}
                <input
                type="password"
                    name="password"
                        onChange={handleChange}
                onBlur={handleBlur}
                value={values.password}
                />
                {touched.password && errors.password && <div>{errors.password}</div>}
                <button type="submit" disabled={isSubmitting}>
                Submit
                </button>
                </form>
        )}
        />
        </div>
);

export default LoginForm;
