import React from 'react';
import {
    BrowserRouter as Router,
    Route,
    Switch
} from "react-router-dom";
import Home  from './home/Home.js';
import Users  from './users/Users.js';

// the routes here do not include the ones whoch do not require authentication
// such as the login page
class AppRoutes extends React.Component {
	render() {
		return (
			<Router>
		        <Switch>
		            <Route exact path="/" component={Home} />
		            <Route exact path="/users" component={Users} />
		        </Switch>
		    </Router>
	    );
	}
}

export default AppRoutes;
