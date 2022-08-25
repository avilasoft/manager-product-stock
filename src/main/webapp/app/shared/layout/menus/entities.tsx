import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/business-associate">
      <Translate contentKey="global.menu.entities.businessAssociate" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/unit">
      <Translate contentKey="global.menu.entities.unit" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/project">
      <Translate contentKey="global.menu.entities.project" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/project-group">
      <Translate contentKey="global.menu.entities.projectGroup" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/project-item-list">
      <Translate contentKey="global.menu.entities.projectItemList" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/project-item-list-item">
      <Translate contentKey="global.menu.entities.projectItemListItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/item">
      <Translate contentKey="global.menu.entities.item" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/provider">
      <Translate contentKey="global.menu.entities.provider" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/provider-item">
      <Translate contentKey="global.menu.entities.providerItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/unit-price-list">
      <Translate contentKey="global.menu.entities.unitPriceList" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/unit-price-list-item">
      <Translate contentKey="global.menu.entities.unitPriceListItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/computed-price-list">
      <Translate contentKey="global.menu.entities.computedPriceList" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/computed-price-list-item">
      <Translate contentKey="global.menu.entities.computedPriceListItem" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
