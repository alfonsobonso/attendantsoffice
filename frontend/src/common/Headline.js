import React, { Component } from 'react';
import PropTypes from 'prop-types';

// material ui components
import Typography from '@material-ui/core/Typography';

import { withStyles } from '@material-ui/core/styles';

// Basic headline with no action attached.
// see also HeadlineWithAction

const styles = theme => ({
    header: {
    	width: '100%',
    	overflowX: 'auto',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-start',
    }
});

class Headline extends Component {

	render() {
		const { classes, headline, headlineVariant, subheading, subheadingVariant } = this.props;

		return (
			<div className={classes.header}>
                <div className={classes.flex}>
                    <Typography variant={headlineVariant || 'h4'} color="inherit">
                        {headline}
                    </Typography>
                    {subheading &&
                        <Typography variant={subheadingVariant || 'subtitle1'} gutterBottom>
                            {subheading}
                        </Typography>
                    }
                </div>
    		</div>
		);
	}
}

Headline.propTypes = {
	classes: PropTypes.object.isRequired,
	theme: PropTypes.object.isRequired,
	headline: PropTypes.oneOfType([ PropTypes.string, PropTypes.object ]).isRequired,
    headlineVariant: PropTypes.string,
    subheading: PropTypes.oneOfType([ PropTypes.string, PropTypes.object ]),
    subheadingVariant: PropTypes.string,
};

export default withStyles(styles, { withTheme: true })(Headline);
