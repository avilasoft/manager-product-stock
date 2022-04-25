import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BusinessAssociate from './business-associate';
import BusinessAssociateDetail from './business-associate-detail';
import BusinessAssociateUpdate from './business-associate-update';
import BusinessAssociateDeleteDialog from './business-associate-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusinessAssociateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusinessAssociateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusinessAssociateDetail} />
      <ErrorBoundaryRoute path={match.url} component={BusinessAssociate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusinessAssociateDeleteDialog} />
  </>
);

export default Routes;
