import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProjectGroup from './project-group';
import ProjectGroupDetail from './project-group-detail';
import ProjectGroupUpdate from './project-group-update';
import ProjectGroupDeleteDialog from './project-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProjectGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProjectGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProjectGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProjectGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProjectGroupDeleteDialog} />
  </>
);

export default Routes;
