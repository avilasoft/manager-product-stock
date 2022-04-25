import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BusinessAssociate from './business-associate';
import Provider from './provider';
import UnitType from './unit-type';
import Unit from './unit';
import Category from './category';
import BatchStatus from './batch-status';
import Batch from './batch';
import ProviderProduct from './provider-product';
import ProviderProductPrice from './provider-product-price';
import BatchProduct from './batch-product';
import ProductCategory from './product-category';
import Product from './product';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}business-associate`} component={BusinessAssociate} />
      <ErrorBoundaryRoute path={`${match.url}provider`} component={Provider} />
      <ErrorBoundaryRoute path={`${match.url}unit-type`} component={UnitType} />
      <ErrorBoundaryRoute path={`${match.url}unit`} component={Unit} />
      <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}batch-status`} component={BatchStatus} />
      <ErrorBoundaryRoute path={`${match.url}batch`} component={Batch} />
      <ErrorBoundaryRoute path={`${match.url}provider-product`} component={ProviderProduct} />
      <ErrorBoundaryRoute path={`${match.url}provider-product-price`} component={ProviderProductPrice} />
      <ErrorBoundaryRoute path={`${match.url}batch-product`} component={BatchProduct} />
      <ErrorBoundaryRoute path={`${match.url}product-category`} component={ProductCategory} />
      <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
