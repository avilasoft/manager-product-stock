import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BusinessAssociate from './business-associate';
import Unit from './unit';
import Project from './project';
import ProjectGroup from './project-group';
import ProjectItemList from './project-item-list';
import ProjectItemListItem from './project-item-list-item';
import Item from './item';
import Provider from './provider';
import ProviderItem from './provider-item';
import UnitPriceList from './unit-price-list';
import UnitPriceListItem from './unit-price-list-item';
import ComputedPriceList from './computed-price-list';
import ComputedPriceListItem from './computed-price-list-item';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}business-associate`} component={BusinessAssociate} />
      <ErrorBoundaryRoute path={`${match.url}unit`} component={Unit} />
      <ErrorBoundaryRoute path={`${match.url}project`} component={Project} />
      <ErrorBoundaryRoute path={`${match.url}project-group`} component={ProjectGroup} />
      <ErrorBoundaryRoute path={`${match.url}project-item-list`} component={ProjectItemList} />
      <ErrorBoundaryRoute path={`${match.url}project-item-list-item`} component={ProjectItemListItem} />
      <ErrorBoundaryRoute path={`${match.url}item`} component={Item} />
      <ErrorBoundaryRoute path={`${match.url}provider`} component={Provider} />
      <ErrorBoundaryRoute path={`${match.url}provider-item`} component={ProviderItem} />
      <ErrorBoundaryRoute path={`${match.url}unit-price-list`} component={UnitPriceList} />
      <ErrorBoundaryRoute path={`${match.url}unit-price-list-item`} component={UnitPriceListItem} />
      <ErrorBoundaryRoute path={`${match.url}computed-price-list`} component={ComputedPriceList} />
      <ErrorBoundaryRoute path={`${match.url}computed-price-list-item`} component={ComputedPriceListItem} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
