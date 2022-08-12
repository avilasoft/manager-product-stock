import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProjectItemListItem from './project-item-list-item';
import ProjectItemListItemDetail from './project-item-list-item-detail';
import ProjectItemListItemUpdate from './project-item-list-item-update';
import ProjectItemListItemDeleteDialog from './project-item-list-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProjectItemListItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProjectItemListItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProjectItemListItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProjectItemListItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProjectItemListItemDeleteDialog} />
  </>
);

export default Routes;
