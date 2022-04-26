import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BatchStatus from './batch-status';
import BatchStatusDetail from './batch-status-detail';
import BatchStatusUpdate from './batch-status-update';
import BatchStatusDeleteDialog from './batch-status-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BatchStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BatchStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BatchStatusDetail} />
      <ErrorBoundaryRoute path={match.url} component={BatchStatus} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BatchStatusDeleteDialog} />
  </>
);

export default Routes;
