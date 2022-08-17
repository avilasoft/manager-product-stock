import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ProjectUpdate from 'app/entities/project/project-update';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>
          <Translate contentKey="footer">Your footer</Translate>
        </p>
      </Col>
    </Row>
  </div>
);

const Wizard = ({ children }) => {
  const [activePageIndex, setActivePageIndex] = React.useState(0);
  const pages = React.Children.toArray(children);
  const currentPage = pages[activePageIndex];

  const goNextPage = () => {
    setActivePageIndex(index => index + 1);
  };

  const goPrevPage = () => {
    setActivePageIndex(index => index - 1);
  };

  const ButtonPrev = () =>
    activePageIndex > 0 ? (
      <button type="button" onClick={goPrevPage} className="wizard__buttons-left">
        Atras
      </button>
    ) : null;
  const ButtonNext = () =>
    activePageIndex < pages.length - 1 ? (
      <button type="button" onClick={goNextPage} className="wizard__buttons-right">
        Siguiente
      </button>
    ) : null;

  return (
    <div className="wizard">
      <div className="wizard__content">{currentPage}</div>
      <div className="wizard__buttons">
        <ButtonPrev />
        <ButtonNext />
      </div>
    </div>
  );
};

const Page1 = () => (
  <div>
    <h1>Paso 1</h1>
    <Link to="project/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
      <FontAwesomeIcon icon="plus" />
      &nbsp;
      <Translate contentKey="managerProductStockApp.project.home.createLabel">Create new Project</Translate>
    </Link>
  </div>
);

const Page2 = () => (
  <div>
    <h1>Paso 2: </h1>
    <Link to="project-item-list/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
      <FontAwesomeIcon icon="plus" />
      &nbsp;
      <Translate contentKey="managerProductStockApp.projectItemList.home.createLabel">Create new Project Item List</Translate>
    </Link>
  </div>
);
const Page3 = () => (
  <div>
    <h1>Paso 3: </h1>
    <Link to="project-item-list-item/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
      <FontAwesomeIcon icon="plus" />
      &nbsp;
      <Translate contentKey="managerProductStockApp.projectItemListItem.home.createLabel">Create new Project Item List Item</Translate>
    </Link>
  </div>
);

const Page4 = () => (
  <div>
    <h1>Paso 4: </h1>
    <Link to="unit-price-list/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
      <FontAwesomeIcon icon="plus" />
      &nbsp;
      <Translate contentKey="managerProductStockApp.unitPriceList.home.createLabel">Create new Unit Price List</Translate>
    </Link>
  </div>
);

const Page5 = () => (
  <div>
    <h1>Paso 5: </h1>
    <Link to="unit-price-list-item/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
      <FontAwesomeIcon icon="plus" />
      &nbsp;
      <Translate contentKey="managerProductStockApp.unitPriceListItem.home.createLabel">Create new Unit Price List Item</Translate>
    </Link>
  </div>
);

const App = () => {
  return (
    <Wizard>
      <Page1 />
      <Page2 />
      <Page3 />
      <Page4 />
      <Page5 />
    </Wizard>
  );
};

export default App;
