import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ComputedPriceList from './computed-price-list';
import ComputedPriceListDetail from './computed-price-list-detail';
import ComputedPriceListUpdate from './computed-price-list-update';
import ComputedPriceListDeleteDialog from './computed-price-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ComputedPriceListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ComputedPriceListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ComputedPriceListDetail} />
      <ErrorBoundaryRoute path={match.url} component={ComputedPriceList} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ComputedPriceListDeleteDialog} />
  </>
);

export default Routes;
