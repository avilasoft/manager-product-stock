import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProjectItemList from './project-item-list';
import ProjectItemListDetail from './project-item-list-detail';
import ProjectItemListUpdate from './project-item-list-update';
import ProjectItemListDeleteDialog from './project-item-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProjectItemListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProjectItemListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProjectItemListDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProjectItemList} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProjectItemListDeleteDialog} />
  </>
);

export default Routes;
