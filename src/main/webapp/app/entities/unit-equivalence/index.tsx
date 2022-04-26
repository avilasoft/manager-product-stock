import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UnitEquivalence from './unit-equivalence';
import UnitEquivalenceDetail from './unit-equivalence-detail';
import UnitEquivalenceUpdate from './unit-equivalence-update';
import UnitEquivalenceDeleteDialog from './unit-equivalence-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UnitEquivalenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UnitEquivalenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UnitEquivalenceDetail} />
      <ErrorBoundaryRoute path={match.url} component={UnitEquivalence} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UnitEquivalenceDeleteDialog} />
  </>
);

export default Routes;
