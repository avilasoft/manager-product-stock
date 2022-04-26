import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BatchProduct from './batch-product';
import BatchProductDetail from './batch-product-detail';
import BatchProductUpdate from './batch-product-update';
import BatchProductDeleteDialog from './batch-product-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BatchProductUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BatchProductUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BatchProductDetail} />
      <ErrorBoundaryRoute path={match.url} component={BatchProduct} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BatchProductDeleteDialog} />
  </>
);

export default Routes;
