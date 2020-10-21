import React from 'react'
import { List, Datagrid, TextField, EditButton, DeleteWithConfirmButton, CreateButton } from 'react-admin'

const UserList = (props) => {
    return (
        <>
            <List {...props}>
                <Datagrid>
                    <TextField source='id'/>
                    <TextField source='name'/>
                    <TextField source='email'/>
                    <TextField source='password'/>
                    <EditButton/>
                    <DeleteWithConfirmButton basePath="users/remove"/>
                </Datagrid>
            </List> 
            <CreateButton/>
        </>
    )
}

export default UserList