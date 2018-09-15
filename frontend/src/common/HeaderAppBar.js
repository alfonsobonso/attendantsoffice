import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import Drawer from '@material-ui/core/Drawer';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import EventIcon from '@material-ui/icons/Event';
import HomeIcon from '@material-ui/icons/Home';
import PeopleIcon from '@material-ui/icons/People';
import Tooltip from '@material-ui/core/Tooltip';

import AuthenticationService from '../authentication/AuthenticationService.js'

const drawerWidth = 240;

const styles = theme => ({
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginLeft: 12,
    marginRight: 30
  },
  logoutButton: {
    marginLeft: 'auto',
  },
  hide: {
    display: 'none',
  },
  drawerPaper: {
    position: 'relative',
    whiteSpace: 'nowrap',
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerPaperClose: {
    overflowX: 'hidden',
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    width: theme.spacing.unit * 7,
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing.unit * 9,
    },
  },
  toolbar: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
});

// menu item , whoch contains the icon, name and link.
// We may render a tooltip (used to only show it when we are in icon-only display mode)
class MenuListItem extends React.PureComponent {
    render() {
        const { title, enabled, link, icon } = this.props;

        if(!enabled) {
            return  (
                <ListItem component={Link} to={link} button>
                    <ListItemIcon>
                        {icon}
                    </ListItemIcon>
                    <ListItemText primary={title} />
                </ListItem>
            )
        }
        return (
            <ListItem component={Link} to={link} button>
                <Tooltip disableFocusListener disableTouchListener title={title}>
                    <ListItemIcon>
                        {icon}
                    </ListItemIcon>
                </Tooltip>
                <ListItemText primary={title} />
            </ListItem>
        )
    }
}

class HeaderAppBar extends Component {
    constructor(props) {
        super();
        this.classes = props.classes;
        this.AuthService = new AuthenticationService();

        // we default the menu open or closed based one the screen size
        let isLarge = window.innerWidth >= 480;
        this.state = { menuOpen: isLarge };
    }

    state = {};

    handleLogout = () => {
        this.AuthService.logout()
        this.props.history.replace('/login');
    }

    toggleDrawer = (open) => () => {
        this.setState({ menuOpen: open });
    };

    render() {
        const { classes, theme } = this.props;
        const menuList = (
            <div className={this.classes.list}>
                <List>
                    <MenuListItem title="Home" link="/" icon={<HomeIcon/>} enabled={!this.state.menuOpen} />
                </List>
                <Divider />
                <List>
                    <MenuListItem title="Users" link="/users" icon={<PeopleIcon/>} enabled={!this.state.menuOpen} />
                    <MenuListItem title="Events" link="/events" icon={<EventIcon/>} enabled={!this.state.menuOpen} />
                </List>
            </div>
        );

        return (
            <React.Fragment>
                <AppBar position="absolute" className={classNames(classes.appBar, this.state.menuOpen && classes.appBarShift)}>
                    <Toolbar disableGutters={!this.state.menuOpen}>
                        <IconButton 
                            color="inherit" 
                            aria-label="Menu"
                            onClick={this.toggleDrawer(true)}
                            className={classNames(classes.menuButton, this.state.menuOpen && classes.hide)}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={this.classes.flex}>
                            Attendants Office
                        </Typography>
                        <Button color="inherit" 
                            onClick={this.handleLogout.bind(this)}
                            className={classNames(classes.logoutButton)}
                        >
                            Logout
                        </Button>
                    </Toolbar>
                </AppBar>
                <Drawer variant="permanent" 
                        classes={{ paper: classNames(classes.drawerPaper, !this.state.menuOpen && classes.drawerPaperClose), }}
                        open={this.state.menuOpen}
                >
                    <div className={classes.toolbar}>
                        <IconButton onClick={this.toggleDrawer(false)}>
                            {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
                        </IconButton>
                    </div>
                    <Divider />
                    <List>{menuList}</List>
                </Drawer>
            </React.Fragment>
        );
    }
}

HeaderAppBar.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired
};

export default withStyles(styles, { withTheme: true })(HeaderAppBar);
