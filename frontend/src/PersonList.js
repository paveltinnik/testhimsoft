import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class PersonList extends Component {

    constructor(props) {
        super(props);
        this.state = {persons: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/api/persons')
            .then(response => response.json())
            .then(data => this.setState({persons: data}));
    }

    async remove(id) {
        await fetch(`/api/persons/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedPersons = [...this.state.persons].filter(i => i.id !== id);
            this.setState({persons: updatedPersons});
        });
    }

    render() {
        const {persons} = this.state;

        const personList = persons.map(person => {
            return <tr key={person.id}>
                <td style={{whiteSpace: 'nowrap'}}>{person.name}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="secondary" tag={Link} to={"/api/persons/" + person.id + "/notes"}>Notes</Button>
                        <Button size="sm" color="primary" tag={Link} to={"/api/persons/" + person.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(person.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/api/persons/new">Add Person</Button>
                    </div>
                    <h3>Persons</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                            {personList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default PersonList;