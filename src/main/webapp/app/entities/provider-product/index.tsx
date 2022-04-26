import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProviderProduct from './provider-product';
import ProviderProductDetail from './provider-product-detail';
import ProviderProductUpdate from './provider-product-update';
import ProviderProductDeleteDialog from './provider-product-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProviderProductUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProviderProductUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProviderProductDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProviderProduct} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProviderProductDeleteDialog} />
  </>
);

export default Routes;
