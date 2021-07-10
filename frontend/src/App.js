import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import NoteList from './NoteList';
import NoteEdit from './NoteEdit';
import PersonList from './PersonList';
import PersonEdit from './PersonEdit';

class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>

                    <Route path='/api/persons/:id/notes' exact={true} component={NoteList}/>
                    <Route path='/api/persons/:personId/notes/:id' component={NoteEdit}/>

                    <Route path='/api/persons' exact={true} component={PersonList}/>
                    <Route path='/api/persons/:id' component={PersonEdit}/>
                </Switch>
            </Router>
        )
    }
}

export default App;