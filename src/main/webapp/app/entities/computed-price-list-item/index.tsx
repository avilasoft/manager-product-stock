import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ComputedPriceListItem from './computed-price-list-item';
import ComputedPriceListItemDetail from './computed-price-list-item-detail';
import ComputedPriceListItemUpdate from './computed-price-list-item-update';
import ComputedPriceListItemDeleteDialog from './computed-price-list-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ComputedPriceListItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ComputedPriceListItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ComputedPriceListItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={ComputedPriceListItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ComputedPriceListItemDeleteDialog} />
  </>
);

export default Routes;
