import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UnitPriceList from './unit-price-list';
import UnitPriceListDetail from './unit-price-list-detail';
import UnitPriceListUpdate from './unit-price-list-update';
import UnitPriceListDeleteDialog from './unit-price-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UnitPriceListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UnitPriceListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UnitPriceListDetail} />
      <ErrorBoundaryRoute path={match.url} component={UnitPriceList} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UnitPriceListDeleteDialog} />
  </>
);

export default Routes;
