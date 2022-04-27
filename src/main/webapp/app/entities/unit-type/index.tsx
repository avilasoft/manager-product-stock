import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UnitType from './unit-type';
import UnitTypeDetail from './unit-type-detail';
import UnitTypeUpdate from './unit-type-update';
import UnitTypeDeleteDialog from './unit-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UnitTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UnitTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UnitTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={UnitType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UnitTypeDeleteDialog} />
  </>
);

export default Routes;
