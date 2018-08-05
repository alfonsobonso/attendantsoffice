import React from 'react';
import {
    BrowserRouter as Router,
    Route,
    Redirect,
    Switch
} from "react-router-dom";
import Home  from './home/Home.js';
import Login  from './login/Login.js';

class AppRoutes extends React.Component {
	constructor(props) {
    	super(props);
    	this.state = {
            authenticated: props.authenticated
        };
    }

    state = {};

	render() {
		return(
			<Router>
				<Switch>
					<PrivateRoute exact path="/" component={Home} isAuthenticated={this.props.authentiated} />
                	<Route exact path="/login" component={Login} />
            	</Switch>
            </Router>
		);
	}
}

const PrivateRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={props =>
      rest.isAuthenticated ? (
        <Component {...props} />
      ) : (
        <Redirect
          to={{
            pathname: "/login",
            state: { from: props.location }
          }}
        />
      )
    }
  />
);

export default AppRoutes;
