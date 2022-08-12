import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProviderItem from './provider-item';
import ProviderItemDetail from './provider-item-detail';
import ProviderItemUpdate from './provider-item-update';
import ProviderItemDeleteDialog from './provider-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProviderItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProviderItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProviderItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProviderItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProviderItemDeleteDialog} />
  </>
);

export default Routes;
