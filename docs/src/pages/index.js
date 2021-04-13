import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from './styles.module.css';

const features = [
  {
    title: 'Easy to Use',
    description: (
      <>
        Easy to implementation with Google Maps SDK, Kotlin Coroutine and Modern Android Architecture.
      </>
    ),
  },
  {
    title: 'Problem Solution',
    description: (
      <>
        This libraries is problem solution for code pain of location watcher, search place location, polyline animation and marker animation (powered by HERE Maps API).
      </>
    ),
  },
  {
    title: 'Advanced Marker Setup',
    description: (
      <>
        Enable creating marker with any view!
      </>
    ),
  },
];

function Feature({imageUrl, title, description}) {
  const imgUrl = useBaseUrl(imageUrl);
  return (
    <div className={clsx('col col--4', styles.feature)}>
      {imgUrl && (
        <div className="text--center">
          <img className={styles.featureImage} src={imgUrl} alt={title} />
        </div>
      )}
      <h3>{title}</h3>
      <p>{description}</p>
    </div>
  );
}

export default function Home() {
  const context = useDocusaurusContext();
  const {siteConfig = {}} = context;
  return (
    <Layout
      title={`${siteConfig.title}`}
      description="Description will go into a meta tag in <head />">
      <header className={clsx('hero hero--primary', styles.heroBanner)}>
        <div className="container">
          <h1 className="hero__title">{siteConfig.title}</h1>
          <p className="hero__subtitle">{siteConfig.tagline}</p>
          <div className={styles.buttons}>
            <Link
              className={clsx(
                'button button--outline button--secondary button--lg',
                styles.getStarted,
              )}
              to={useBaseUrl('docs/')}>
              Get Started
            </Link>
          </div>
          <div>
            <br></br>
            <a href="https://jitpack.io/#utsmannn/geolib">
              <img src="https://jitpack.io/v/utsmannn/geolib.svg"></img> &nbsp;
            </a>

            <a href="https://github.com/utsmannn/geolib">
              <img src="https://img.shields.io/github/forks/utsmannn/geolib.svg?style=social"></img> &nbsp;
            </a>
            <a href="https://github.com/utsmannn/geolib">
              <img src="https://img.shields.io/github/stars/utsmannn/geolib.svg?style=social"></img>
            </a>
          </div>
        </div>
      </header>
      <main>
        {features && features.length > 0 && (
          <section className={styles.features}>
            <div className="container">
              <div className="row">
                {features.map((props, idx) => (
                  <Feature key={idx} {...props} />
                ))}
              </div>
            </div>
          </section>
        )}
      </main>
    </Layout>
  );
}
