import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UnitPriceListItem from './unit-price-list-item';
import UnitPriceListItemDetail from './unit-price-list-item-detail';
import UnitPriceListItemUpdate from './unit-price-list-item-update';
import UnitPriceListItemDeleteDialog from './unit-price-list-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UnitPriceListItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UnitPriceListItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UnitPriceListItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={UnitPriceListItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UnitPriceListItemDeleteDialog} />
  </>
);

export default Routes;
