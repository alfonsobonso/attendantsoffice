import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import classnames from 'classnames';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

const styles = theme => ({
    cardContent: {
        paddingTop: '0px',
    },
    actions: {
        display: 'flex',
    },
    expand: {
        transform: 'rotate(0deg)',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
        marginLeft: 'auto',
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
});

class InformationCard extends React.Component {
  state = { expanded: true };

  handleExpandClick = () => {
    this.setState(state => ({ expanded: !state.expanded }));
  };

  render() {
    const { classes } = this.props;

    return (
      <Card>
        <CardHeader
            action={
                <IconButton
                    className={classnames(classes.expand, {
                        [classes.expandOpen]: this.state.expanded,
                    })}
                    onClick={this.handleExpandClick}
                    aria-expanded={this.state.expanded}
                    aria-label="Show more"
                >
                    <ExpandMoreIcon />
                </IconButton>
                }
            title="Thank you..."
        />
        <Collapse in={this.state.expanded} timeout="auto" unmountOnExit>
          <CardContent className={classes.cardContent}>
            <Typography component="p">
                Dear Brothers and Sisters
                <br/><br/>
                We are sure that everyone thoroughly enjoyed our convention and feel encouraged and upbuilt. 
                Every year we say that was the ‘best one yet’ and that just shows Jehovah knows what we need at the right time.
                <br/><br/>
                We would like to thank everyone for your dedication and hard work in caring for our brothers and sisters over the weekend. 
                Many were also involved In helping with the preparation in the lead up to the convention and this was also appreciated as 
                it was a sacrifice of your personal time, for many weeks.
                <br/><br/>
				We hope you enjoyed your assignment, and if so, please encourage others to speak to their Body of Elders about 
				volunteering and working with us next year.
				<br/>
				It seems a long way off, but if it is Jehovah’s will, our 2019 Convention will soon be upon us and we look forward to 
				working with all again...!
				<br/><br/>
				Your brothers
                <br/>
				Attendants Department ​
            </Typography>
          </CardContent>
        </Collapse>
      </Card>
    );
  }
}

InformationCard.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(InformationCard);