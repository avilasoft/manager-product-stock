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
    <MenuItem icon="asterisk" to="/provider">
      <Translate contentKey="global.menu.entities.provider" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/unit-type">
      <Translate contentKey="global.menu.entities.unitType" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/unit">
      <Translate contentKey="global.menu.entities.unit" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/batch-status">
      <Translate contentKey="global.menu.entities.batchStatus" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/batch">
      <Translate contentKey="global.menu.entities.batch" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/provider-product">
      <Translate contentKey="global.menu.entities.providerProduct" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/provider-product-price">
      <Translate contentKey="global.menu.entities.providerProductPrice" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/batch-product">
      <Translate contentKey="global.menu.entities.batchProduct" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-category">
      <Translate contentKey="global.menu.entities.productCategory" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/price">
      <Translate contentKey="global.menu.entities.price" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product">
      <Translate contentKey="global.menu.entities.product" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
