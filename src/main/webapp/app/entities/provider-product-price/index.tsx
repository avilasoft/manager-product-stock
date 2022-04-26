import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProviderProductPrice from './provider-product-price';
import ProviderProductPriceDetail from './provider-product-price-detail';
import ProviderProductPriceUpdate from './provider-product-price-update';
import ProviderProductPriceDeleteDialog from './provider-product-price-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProviderProductPriceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProviderProductPriceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProviderProductPriceDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProviderProductPrice} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProviderProductPriceDeleteDialog} />
  </>
);

export default Routes;
