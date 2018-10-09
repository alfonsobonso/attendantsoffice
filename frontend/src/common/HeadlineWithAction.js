import React, { Component } from 'react';
import PropTypes from 'prop-types';

import classNames from 'classnames';

// material ui components
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import { withStyles } from '@material-ui/core/styles';

// Headline with an action attached.
// see also Headline

const styles = theme => ({
    header: {
    	width: '100%',
    	overflowX: 'auto',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
    },
    headerButton: {
        marginLeft: 'auto',
    }
});

class HeadlineWithAction extends Component {

	render() {
		const { classes, headline, headlineVariant, subheading, subheadingVariant, 
                buttonLabel, buttonOnClick } = this.props;

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
        		<Button variant="raised" color="primary"
            		onClick={buttonOnClick}
            		className={classNames(classes.headerButton)}
        		>
            		{buttonLabel}
        		</Button>
    		</div>
		);
	}
}

HeadlineWithAction.propTypes = {
	classes: PropTypes.object.isRequired,
	theme: PropTypes.object.isRequired,
	headline: PropTypes.oneOfType([ PropTypes.string, PropTypes.object ]).isRequired,
    headlineVariant: PropTypes.string,
    subheading: PropTypes.oneOfType([ PropTypes.string, PropTypes.object ]),
    subheadingVariant: PropTypes.string,
	buttonLabel: PropTypes.string.isRequired,
	buttonOnClick: PropTypes.func.isRequired,
};

export default withStyles(styles, { withTheme: true })(HeadlineWithAction);
