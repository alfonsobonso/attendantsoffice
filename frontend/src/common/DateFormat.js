import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { FormattedDate } from 'react-intl';

// Date format in dd-MMMM-yyyy format, e.g 16 July 2019
class DateFormat extends Component {
	render() {
		let date = this.props.date;
		return (<FormattedDate value={date} day="numeric" month="long" year="numeric" />);
	}
}

DateFormat.propTypes = {
    //date: PropTypes.instanceOf(Date).isRequired,
    date: PropTypes.string.isRequired,
};

export default DateFormat;