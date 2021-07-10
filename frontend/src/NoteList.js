import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class NoteList extends Component {

    constructor(props) {
        super(props);
        this.state = {notes: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/api/persons/' + this.props.match.params.id + '/notes')
            .then(response => response.json())
            .then(data => this.setState({notes: data}));
    }

    async remove(id) {
        const personId = this.props.match.params.id;

        await fetch(`/api/persons/${personId}/notes/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedNotes = [...this.state.notes].filter(i => i.id !== id);
            this.setState({notes: updatedNotes});
        });
    }

    render() {
        const {notes} = this.state;

        const noteList = notes.map(note => {
            return <tr key={note.id}>
                <td style={{whiteSpace: 'nowrap'}}>{note.title}</td>
                <td>{note.text}</td>
                <td>{note.createdAt}</td>
                <td>{note.notifyAt}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary"
                                tag={Link} to={"/api/persons/" +
                        this.props.match.params.id + "/notes/" + note.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(note.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to={"/api/persons/" +
                        this.props.match.params.id + "/notes/new"}>Add Notes</Button>
                    </div>
                    <h3>Notes</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%">Title</th>
                            <th width="20%">Text</th>
                            <th width="20%">CreatedAt</th>
                            <th width="20%">NotifyAt</th>
                            <th width="20%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                            {noteList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default NoteList;