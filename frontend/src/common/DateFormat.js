import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { FormattedDate } from 'react-intl';

class DateFormat extends Component {
	render() {
		return (<FormattedDate value={new Date()} day="numeric" month="long" year="numeric" />);
	}
}

DateFormat.propTypes = {
    //date: PropTypes.instanceOf(Date).isRequired,
    date: PropTypes.string.isRequired,
};

export default DateFormat;